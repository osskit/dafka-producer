package ports

import endpoints4s.algebra

trait MonitoringEndpoint  extends algebra.Endpoints{

  val alive : Endpoint[Unit, String] =
    endpoint(
      get( path / "alive"),
      ok(textResponse)
    )

  val ready: Endpoint[Unit, String] =
    endpoint(
      get(path / "ready"),
      ok(textResponse)
    )
}
