package adapters

import cats.implicits._
import cats.effect.IO
import com.banno.kafka.producer.ProducerApi
import endpoints4s.http4s.server.{Endpoints, JsonEntitiesFromCodecs}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.Header
import org.apache.kafka.common.header.internals.RecordHeader
import org.http4s.HttpRoutes
import org.typelevel.log4cats.slf4j.Slf4jLogger
import ports.ProducerResponse

import scala.jdk.CollectionConverters._

class ProduceService(producer: ProducerApi[IO, String, String]) extends
  Endpoints[IO]
   with JsonEntitiesFromCodecs
  with ports.ProduceEndpoint {

  val service : org.http4s.HttpRoutes[IO] = HttpRoutes.of(
    routesFromEndpoints(
      produce.implementedByEffect(records=>
        for {
          logger <- Slf4jLogger.create[IO]
          _ <- logger.info("handling produce requests")
          _ <- producer.beginTransaction
          _ <- records
            .map(record =>
              producer.sendAsync(
                new ProducerRecord(
                  record.topic,
                  null,
                  record.key.getOrElse(null),
                  record.value.toString(),
                  record.headers.map(headers => headers.map {
                    case (key, value) =>
                      val header: Header = new RecordHeader(key, value.getBytes)
                      header
                  }).map(seq => seq.asJava).getOrElse(null)
                )
              )
            )
            .parSequence
          _ <- producer.commitTransaction
          _ <- logger.info("messages produced successfully")
        } yield (ProducerResponse(true))
      )
    )
  )
}
