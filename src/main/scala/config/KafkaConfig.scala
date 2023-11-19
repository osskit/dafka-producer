package config

import cats.implicits._
import ciris.refined._
import ciris._
import enumeratum.values.{StringCirisEnum, StringEnum, StringEnumEntry}
import eu.timepit.refined.types.string.NonEmptyString

import java.nio.file.Path

final case class KafkaConfig(readinessTopic: Option[String], producerConfig: Seq[(String, AnyRef)])

sealed abstract class SASLMechanism(val value: String) extends StringEnumEntry
object SASLMechanism extends StringEnum[SASLMechanism] with StringCirisEnum[SASLMechanism] {
  case object Plain extends SASLMechanism("PLAIN")
  case object Scram extends SASLMechanism("SCRAM-SHA-512")

  val values: IndexedSeq[SASLMechanism] = findValues
}

final case class SASLConfig(username: NonEmptyString, password: Secret[String], mechanism: SASLMechanism, truststoreFilePath: Option[Path], truststoreFilePassword: Option[Secret[String]])

object KafkaConfig {
  implicit val pathDecoder: ConfigDecoder[String, Path] =
    ConfigDecoder[String, String]
      .map(path => Path.of(path))

  def applySASLConfig() : ConfigValue[Effect, SASLConfig] = (
    env("SASL_USERNAME").as[NonEmptyString],
    env("SASL_PASSWORD").or(
      env("SASL_PASSWORD_FILE_PATH").as[Path].flatMap(file)
    ).secret,
    env("SASL_MECHANISM").as[SASLMechanism],
    env("TRUSTSTORE_FILE_PATH").as[Path].option,
    env("TRUSTSTORE_PASSWORD")
      .or(
        env("TRUSTSTORE_PASSWORD_FILE_PATH").as[Path].flatMap(file)
      ).secret.option,
  ).parMapN(SASLConfig.apply)

  def apply(): ConfigValue[Effect, KafkaConfig] = (
    env("KAFKA_BROKER"),
    env("READINESS_TOPIC").option,
    env("USE_SASL_AUTH").flatMap(_ => applySASLConfig()).option,
    env("COMPRESSION_TYPE").default("none"),
    env("LINGER_TIME_MS").default("0"),
    env("BATCH_SIZE").option,
    env("MAX_BLOCK_MS").default("60000"),
    env("USE_GROUP_PARTITIONER").as[Boolean].default(false),
  ).parMapN{
    case (broker, readinessTopic, saslConfig, compressionType, lingerTime, batchSize, maxBlockMS, useGroupPartitioner) => {

      val producerConfig = saslConfig.map(sasl => {
        val truststore = sasl.truststoreFilePath zip sasl.truststoreFilePassword
        val kafkaSecurity = sasl.mechanism match {
          case SASLMechanism.Plain => "plain.PlainLoginModule"
          case SASLMechanism.Scram => "scram.ScramLoginModule"
        }

        truststore.map{ case(path, password) =>
          Seq(
            ("ssl.truststore.location", path.toString),
            ("ssl.truststore.password", password.value),
            ("ssl.protocol", "TLS")
          )
        }.getOrElse(Seq()) ++ Seq(
          ("security.protocol", "SASL_SSL"),
          ("sasl.mechanism", sasl.mechanism.value),
          ("sasl.jaas.config",
            s"org.apache.kafka.common.security.$kafkaSecurity required username=\"${sasl.username}\" password=\"${sasl.password.value}\";"
          ),
          ("compression.type", compressionType)
        )
      }).getOrElse(Seq()) ++
        Seq(
          ("key.serializer", "org.apache.kafka.common.serialization.StringSerializer"),
          ("value.serializer", "org.apache.kafka.common.serialization.StringSerializer"),
          ("bootstrap.servers", broker),
          ("linger.ms", lingerTime),
          ("max.block.ms", maxBlockMS),
        ) ++
        batchSize.map(size => Seq(("batch.size", size))).getOrElse(Seq()) ++
        (if (useGroupPartitioner) Seq(("partitioner.class", "partitioners.GroupPartitioner")) else Seq.empty)

      KafkaConfig(readinessTopic, producerConfig)
    }
  }
}