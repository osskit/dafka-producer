package adapters
package endpoints

import cats.effect.IO
import endpoints4s.http4s.server.{Endpoints, JsonEntitiesFromSchemas}
import org.http4s.HttpRoutes
import ports.{MonitoringEndpoint, Producer}

class MonitoringService(producer: Producer)
  extends Endpoints[IO]
    with MonitoringEndpoint
    with JsonEntitiesFromSchemas {

  private def healthy (): IO[Boolean] = IO.interruptibleMany{
    producer
      .healthy()
  }.flatMap(x => x)

  val routes: HttpRoutes[IO] =
    HttpRoutes.of(
      routesFromEndpoints(
        ready.implementedByEffect(_ => healthy()),
        alive.implementedByEffect(_ => healthy())
      )
    )

}
