package config

import ciris.{ConfigValue, Effect, env}

case class PrometheusConfig(enableDefaultMetrics: Boolean)

object PrometheusConfig {

  def apply: ConfigValue[Effect, PrometheusConfig] = (
    env("PROMETHEUS_EXPOSE_JAVA_METRICS").as[Boolean].default(false)
    ).map(enableDefaultMetrics => PrometheusConfig(enableDefaultMetrics))
}
