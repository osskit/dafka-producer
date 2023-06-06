package adapters.endpoints

import cats.effect.IO
import cats.implicits._
import endpoints4s.http4s.server.{Endpoints, JsonEntitiesFromCodecs}
import org.http4s.HttpRoutes
import org.typelevel.log4cats.{LoggerFactory, SelfAwareStructuredLogger}
import org.typelevel.log4cats.slf4j.Slf4jLogger
import ports.{Producer, ProducerResponse}

class ProduceService(
                      producer: Producer)
                    (implicit val loggerFactory: LoggerFactory[IO]) extends
  Endpoints[IO]
   with JsonEntitiesFromCodecs
  with ports.ProduceEndpoint {

  private val logger: SelfAwareStructuredLogger[IO] = LoggerFactory[IO].getLogger

  val routes : org.http4s.HttpRoutes[IO] = HttpRoutes.of(
    routesFromEndpoints(
      produce.implementedByEffect(records=>
        (for {
          _ <- logger.info("handling produce requests")
          _ <- records
            .map(record =>
              producer.produce(record)
            )
            .parSequence
          _ <- logger.info("messages produced successfully")
        } yield ProducerResponse(true)).handleErrorWith{throwable =>
          logger.error(throwable)("failed to handle producer request") *> IO.pure(ProducerResponse(false))
        }
    )
  )
  )
}
