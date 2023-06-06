package adapters.kafka

import adapters.Monitor
import cats.effect.IO
import com.banno.kafka.producer.ProducerApi
import io.prometheus.client.CollectorRegistry
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.Header
import org.apache.kafka.common.header.internals.RecordHeader
import org.typelevel.log4cats.{LoggerFactory, SelfAwareStructuredLogger}
import ports.{ProduceRequest, Producer}

import java.util.Date
import scala.jdk.CollectionConverters._

class ProducerImpl(
                    producer: ProducerApi[IO, String, String],
                    readinessTopic: Option[String])
                  (implicit registrar: CollectorRegistry, implicit val loggerFactory: LoggerFactory[IO])  extends Producer{

  implicit val logger: SelfAwareStructuredLogger[IO] = LoggerFactory[IO].getLogger

  private val monitor = new Monitor("producer", Seq("topic"))

  override def produce(record: ProduceRequest): IO[Unit] =
    monitor.monitor("produce", Seq(record.topic), Map(
      "topic" -> record.topic,
      "message" -> record.value.toString(),
      "key" -> record.key.getOrElse("undefined")
    ))(()=>
    producer.sendAsync(
      new ProducerRecord(
        record.topic,
        null,
        new Date().getTime(),
        record.key.orNull,
        record.value.toString(),
        record.headers.map(headers => headers.map {
          case (key, value) =>
            val header: Header = new RecordHeader(key, value.getBytes)
            header
        }).map(seq => seq.asJava).orNull
      )
    )).map(_ => ())

  override def healthy(): IO[Boolean] = {
    readinessTopic.map { topic =>
      producer
        .partitionsFor(topic)
        .map(_ => true)
        .handleErrorWith(throwable =>
        logger.error(throwable)("producer health check failed") *> IO.pure(false))
    }.getOrElse(IO.pure(true))
  }
}
