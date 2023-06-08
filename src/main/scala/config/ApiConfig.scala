package config

import cats.implicits._
import ciris.{ConfigDecoder, ConfigValue, Effect, env}
import com.comcast.ip4s.{Port, _}

case class ApiConfig(port: Port)

object ApiConfig {
  implicit val portDecoder : ConfigDecoder[String, Port] =
    ConfigDecoder[String, String]
      .mapOption("Port")(Port.fromString)


  def apply: ConfigValue[Effect, ApiConfig] = (
    env("PORT").as[Port].default(port"8080")
    ).map(port => ApiConfig(port))
}
