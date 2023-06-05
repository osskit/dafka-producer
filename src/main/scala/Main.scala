import cats.effect._
import com.banno.kafka.{BootstrapServers, ClientId, KeySerializerClass, SchemaRegistryUrl, TransactionalId, ValueSerializerClass}
import org.http4s._
import com.comcast.ip4s._
import cats.implicits._
import org.http4s.implicits._
import org.http4s.ember.server._
import org.http4s.server.Router
import endpoints4s.http4s.server.Endpoints
import com.banno.kafka.producer._
import org.apache.kafka.clients.producer.ProducerRecord
import ports.ProducerResponse
import config.Config.build
import org.http4s.metrics.MetricsOps
import org.http4s.metrics.prometheus.{Prometheus, PrometheusExportService}
import org.http4s.server.middleware.Metrics
import adapters.ProduceService
import org.apache.kafka.common.serialization.StringSerializer
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.apache.kafka.common.serialization.Serializer
import java.net.InetAddress

object Main extends IOApp {
  def httpServer(service: ProduceService, metrics: MetricsOps[IO], metricsRoute: HttpRoutes[IO]) = {
    val routes = Metrics[IO](metrics)(service.service) <+> metricsRoute
    val httpApp = Router("/" -> routes).orNotFound
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(httpApp)
      .build
  }

  val ia: InetAddress = InetAddress.getLocalHost
  val hostname: String = ia.getHostName

  implicit val serializer : Serializer[String] =new StringSerializer()

  val resources =
    for {
      metricsSvc <- PrometheusExportService.build[IO]
      metrics <- Prometheus.metricsOps[IO](metricsSvc.collectorRegistry, "dafka_producer")
      config <- build().resource[IO]
      producer <- ProducerApi.resource[IO, String, String](
        BootstrapServers(config.kafka.broker),
        ClientId("dafka-producer"),
        TransactionalId(hostname),
        KeySerializerClass( classOf[StringSerializer]),
        ValueSerializerClass( classOf[StringSerializer])
      ).evalMap(producer =>
        producer.initTransactions.map(_ => producer)
      )
      service = new ProduceService(producer)
      service <- httpServer(service, metrics, metricsSvc.routes)
    } yield (service)

  def run(args: List[String]): IO[ExitCode] = resources.use(_ =>
  for {
    logger <- Slf4jLogger.create[IO]
    _ <- logger.info("dafka-producer started")
    _ <- IO.never[ExitCode]
  } yield ExitCode.Success
  )
}