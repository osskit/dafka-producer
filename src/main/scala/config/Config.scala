package config
import cats.syntax.all._
import ciris._
import ciris.refined._
import eu.timepit.refined.types.string.NonEmptyString

final case class Config(
                         name: NonEmptyString,
                         kafka: KafkaConfig,
                       apiConfig: ApiConfig,
                         prometheusConfig: PrometheusConfig
                       )

object Config {
  def build(): ConfigValue[Effect, Config] =
    (
      default("my-api").as[NonEmptyString],
      KafkaConfig.apply(),
      ApiConfig.apply,
      PrometheusConfig.apply
    ).parMapN((name, kafkaConfig, apiConfig, prometheusConfig) => Config(name, kafkaConfig, apiConfig, prometheusConfig))
}

