package config
import cats.syntax.all._
import ciris.refined._
import ciris._
import com.comcast.ip4s.Port
import eu.timepit.refined.types.string.NonEmptyString

final case class Config(
                         name: NonEmptyString,
                         kafka: KafkaConfig,
                       apiConfig: ApiConfig
                       )

object Config {
  def build(): ConfigValue[Effect, Config] =
    (
      default("my-api").as[NonEmptyString],
      KafkaConfig.apply,
      ApiConfig.apply,
    ).parMapN((name, kafkaConfig, apiConfig) => Config(name, kafkaConfig, apiConfig))
}

