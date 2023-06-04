package ports

import cats.effect.IO
import endpoints4s.{algebra, generic}

case class ProduceRequest(topic: String, value: String)
case class ProducerResponse(ok: Boolean)

trait ProduceEndpoint extends algebra.Endpoints
  with algebra.JsonEntitiesFromSchemas
  with generic.JsonSchemas {

  implicit lazy val produceRequestSchema : JsonSchema[ProduceRequest] = genericJsonSchema
  implicit lazy val produceResponseSchema : JsonSchema[ProducerResponse] = genericJsonSchema

  val produce : Endpoint[ProduceRequest, ProducerResponse] =
    endpoint(
      post(path / "produce", jsonRequest[ProduceRequest]),
      ok(jsonResponse[ProducerResponse])
    )

}
