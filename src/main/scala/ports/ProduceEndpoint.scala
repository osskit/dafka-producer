package ports

import cats.effect.IO
import endpoints4s.{algebra, generic}
import endpoints4s.algebra.circe.JsonEntitiesFromCodecs
import io.circe.Json

import io.circe.generic.auto._, io.circe.syntax._
case class ProduceRequest(topic: String, value: Json, key: Option[String], headers: Option[Map[String, String]])
case class ProducerResponse(ok: Boolean)

trait ProduceEndpoint extends algebra.Endpoints
  with JsonEntitiesFromCodecs {

  val produce : Endpoint[Seq[ProduceRequest], ProducerResponse] =
    endpoint(
      post(path / "produce", jsonRequest[Seq[ProduceRequest]]),
      ok(jsonResponse[ProducerResponse])
    )

}
