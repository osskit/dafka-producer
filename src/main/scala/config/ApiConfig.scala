package config

import ciris.{ConfigDecoder, ConfigValue, Effect, env}
import cats.implicits._
import com.comcast.ip4s.Port
import com.comcast.ip4s._

case class ApiConfig(port: Port)

object ApiConfig {
  implicit val portDecoder : ConfigDecoder[String, Port] =
    ConfigDecoder[String, String]
      .mapOption("Port")(Port.fromString)


  def apply: ConfigValue[Effect, ApiConfig] = (
    env("PORT").as[Port].default(port"8080")
    ).map(port => ApiConfig(port))
}
