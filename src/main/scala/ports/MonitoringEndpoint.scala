package ports

import endpoints4s.algebra

trait MonitoringEndpoint  extends algebra.Endpoints
    with algebra.JsonEntitiesFromSchemas{

  val alive : Endpoint[Unit, Boolean] =
    endpoint(
      get( path / "alive"),
      ok(jsonResponse[Boolean])
    )

  val ready: Endpoint[Unit, Boolean] =
    endpoint(
      get(path / "ready"),
      ok(jsonResponse[Boolean])
    )
}
