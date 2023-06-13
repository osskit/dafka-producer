import adapters.endpoints.{MonitoringService, ProduceService}
import adapters.kafka.ProducerImpl
import cats.effect._
import cats.effect.metrics.CpuStarvationWarningMetrics
import cats.effect.unsafe.IORuntimeConfig
import cats.implicits._
import com.banno.kafka.producer._
import com.comcast.ip4s._
import config.Config.build
import org.apache.kafka.common.serialization.{Serializer, StringSerializer}
import org.http4s._
import org.http4s.ember.server._
import org.http4s.implicits._
import org.http4s.metrics.prometheus.{Prometheus, PrometheusExportService}
import org.http4s.server.Router
import org.http4s.server.middleware.Metrics
import org.typelevel.log4cats.{LoggerFactory, SelfAwareStructuredLogger}
import org.typelevel.log4cats.slf4j.{Slf4jFactory, Slf4jLogger}

import scala.concurrent.duration._

object Main extends IOApp.Simple {
  implicit val serializer: Serializer[String] = new StringSerializer()
  implicit val logging: LoggerFactory[IO] = Slf4jFactory.create[IO]
  private val logger: SelfAwareStructuredLogger[IO] = LoggerFactory[IO].getLogger

  // :vomit: https://github.com/typelevel/cats-effect/issues/3687
  override protected def blockedThreadDetectionEnabled = sys.env.get("BLOCKED_THREAD_DETECTION_ENABLED") match {
    case Some(_) => true
    case None => false
  }

  private val cpuStarvationCheckInitialDelay = sys.env.get("CPU_STARVATION_CHECK_INITIAL_DELAY_MS") match {
    case None => 10.seconds
    case Some(value) => Duration.create(value.toInt, "millis")
  }

  private val cpuStarvationCheckInterval = sys.env.get("CPU_STARVATION_CHECK_INTERVAL_MS") match {
    case None => 1.seconds
    case Some(value) => Duration.create(value.toInt, "millis")
  }

  private val cpuStarvationCheckThreshold = sys.env.get("CPU_STARVATION_CHECK_THRESHOLD") match {
    case None => 0.1d
    case Some(value) => value.toDouble
  }

  override def runtimeConfig: IORuntimeConfig =
    super.runtimeConfig.copy(
      cpuStarvationCheckInitialDelay = cpuStarvationCheckInitialDelay,
      cpuStarvationCheckInterval = cpuStarvationCheckInterval,
      cpuStarvationCheckThreshold = cpuStarvationCheckThreshold
    )

  private def mkWarning(threshold: Duration): String =
    s"""|Your app's responsiveness to a new asynchronous
        | event (such as a new connection, an upstream response, or a timer) was in excess
        | of $threshold. Your CPU is probably starving. Consider increasing the
        | granularity of your delays or adding more cedes. This may also be a sign that you
        | are unintentionally running blocking I/O operations (such as File or InetAddress)
        | without the blocking combinator.""".stripMargin.replaceAll("\n", "")

  override protected def onCpuStarvationWarn(metrics: CpuStarvationWarningMetrics): IO[Unit] =
    logger.info(Map(
      "occurrenceTime" -> metrics.occurrenceTime.toString()
    ))(mkWarning(metrics.starvationInterval * metrics.starvationThreshold))

  private def httpServer(routes: HttpRoutes[IO], port: Port): Resource[IO, server.Server] = {
    val httpApp = Router("/" -> routes).orNotFound
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port)
      .withHttpApp(httpApp)
      .build
  }

  private val resources =
    for {
      metricsSvc <- PrometheusExportService.build[IO]
      metrics <- Prometheus.metricsOps[IO](metricsSvc.collectorRegistry, "dafka_producer")
      config <- build().resource[IO]
      producer <- ProducerApi.resource[IO, String, String](
        config.kafka.producerConfig: _*,
      )
      producerImpl = new ProducerImpl(producer, config.kafka.readinessTopic)(metricsSvc.collectorRegistry, logging)
      produceService = new ProduceService(producerImpl)
      monitoringService = new MonitoringService(producerImpl)
      service <- httpServer(Metrics[IO](metrics)(produceService.routes) <+> monitoringService.routes <+>  metricsSvc.routes, config.apiConfig.port)
    } yield (service)

  def run = resources.use(_ =>
  for {
    logger <- Slf4jLogger.create[IO]
    _ <- logger.info("dafka-producer started")
    _ <- IO.never[ExitCode]
  } yield ()
  )
}