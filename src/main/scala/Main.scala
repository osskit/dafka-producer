import adapters.endpoints.{MonitoringService, ProduceService}
import adapters.kafka.ProducerImpl
import cats.effect._
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
import org.typelevel.log4cats.LoggerFactory
import org.typelevel.log4cats.slf4j.{Slf4jFactory, Slf4jLogger}

object Main extends IOApp {
  private def httpServer(routes: HttpRoutes[IO], port: Port): Resource[IO, server.Server] = {
    val httpApp = Router("/" -> routes).orNotFound
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port)
      .withHttpApp(httpApp)
      .build
  }

  implicit val serializer : Serializer[String] =new StringSerializer()
  implicit val logging: LoggerFactory[IO] = Slf4jFactory.create[IO]

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

  def run(args: List[String]): IO[ExitCode] = resources.use(_ =>
  for {
    logger <- Slf4jLogger.create[IO]
    _ <- logger.info("dafka-producer started")
    _ <- IO.never[ExitCode]
  } yield ExitCode.Success
  )
}