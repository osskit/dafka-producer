package config
import ciris._
import ciris.{ConfigValue, Effect}
import cats.syntax.all._
import ciris.refined._
import enumeratum.{CirisEnum, Enum, EnumEntry}
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.collection.MinSize
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.types.net.UserPortNumber
import eu.timepit.refined.types.string.NonEmptyString
import scala.concurrent.duration._

final case class Config(
                         name: NonEmptyString,
                         kafka: KafkaConfig
                       )

object Config {
  def build(): ConfigValue[Effect, Config] =
    (
      default("my-api").as[NonEmptyString],
      env("APP_ENV").as[AppEnvironment].flatMap(KafkaConfig.apply),
    ).parMapN((name, kafkaConfig) => Config(name, kafkaConfig))
}

