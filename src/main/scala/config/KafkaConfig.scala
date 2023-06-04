package config

import ciris.{ConfigValue, Effect, env}
import config.AppEnvironment

final case class KafkaConfig(broker: String)

object KafkaConfig {
  def apply(environment: AppEnvironment): ConfigValue[Effect, KafkaConfig] = (
    env("KAFKA_BROKER")
  ).map(broker => KafkaConfig(broker))
}