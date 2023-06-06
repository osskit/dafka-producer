package adapters
package endpoints

import cats.effect.IO
import cats.implicits._
import endpoints4s.http4s.server.Endpoints
import org.http4s.HttpRoutes
import ports.{MonitoringEndpoint, Producer}

class MonitoringService(producer: Producer) extends Endpoints[IO] with MonitoringEndpoint{
  val routes =
    HttpRoutes.of(
      routesFromEndpoints(
        ready.implementedByEffect(_ => producer.healthy().map(res => res.toString))
      )
    ) <+>
    HttpRoutes.of(
      routesFromEndpoints(
        alive.implementedByEffect(_ => producer.healthy().map(res => res.toString))
      )
    )

}
