package adapters.endpoints

import cats.effect.IO
import cats.implicits._
import endpoints4s.http4s.server.{Endpoints, JsonEntitiesFromCodecs}
import org.http4s.HttpRoutes
import org.typelevel.log4cats.slf4j.Slf4jLogger
import ports.{Producer, ProducerResponse}

class ProduceService(
                      producer: Producer) extends
  Endpoints[IO]
   with JsonEntitiesFromCodecs
  with ports.ProduceEndpoint {


  val routes : org.http4s.HttpRoutes[IO] = HttpRoutes.of(
    routesFromEndpoints(
      produce.implementedByEffect(records=>
       for {
          logger <- Slf4jLogger.create[IO]
          _ <- logger.info("handling produce requests")
          _ <- records
            .map(record =>
              producer.produce(record)
            )
            .parSequence
          _ <- logger.info("messages produced successfully")
        } yield ProducerResponse(true)
    )
  )
  )
}
