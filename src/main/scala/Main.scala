import cats.effect._
import com.banno.kafka.BootstrapServers
import org.http4s._
import com.comcast.ip4s._
import org.http4s.ember.server._
import org.http4s.server.Router
import endpoints4s.http4s.server.Endpoints
import com.banno.kafka.producer._
import org.apache.kafka.clients.producer.ProducerRecord
import ports.ProducerResponse
import config.Config.build

class Service(producer: ProducerApi[IO, String, String]) extends Endpoints[IO] with endpoints4s.http4s.server.JsonEntitiesFromSchemas with ports.ProduceEndpoint {
  val producerResource = ProducerApi.Avro.resource[IO, String, String](
    BootstrapServers(""),
  )

  val service : org.http4s.HttpRoutes[IO] = HttpRoutes.of(
    routesFromEndpoints(
      produce.implementedByEffect(x=> {
        for {
          _ <- producer.sendAsync(new ProducerRecord(x.topic, x.value))
        } yield (ProducerResponse(true))
      })
    )
  )
}

object hello extends IOApp {
/*
  val httpApp = Router("/" -> Service.service).orNotFound
  val server = EmberServerBuilder
    .default[IO]
    .withHost(ipv4"0.0.0.0")
    .withPort(port"8080")
    .withHttpApp(httpApp)
    .build
*/

  def httpServer(service: Service) = {
    val httpApp = Router("/" -> service.service).orNotFound
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(httpApp)
      .build
  }


  val resources =
    for {
      config <- build().resource[IO]
      producer <- ProducerApi.Avro.resource[IO, String, String](
        BootstrapServers(config.kafka.broker))
      service = new Service(producer)
      service <- httpServer(service)
    } yield (service)

  def run(args: List[String]): IO[ExitCode] = resources.use(_ => IO.never).as(ExitCode.Success)
}