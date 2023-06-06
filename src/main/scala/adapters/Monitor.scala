package adapters

import cats.effect.IO
import io.prometheus.client.{CollectorRegistry, Counter, Histogram}
import org.typelevel.log4cats.SelfAwareStructuredLogger

class Monitor(
               scope: String,
               labelNames: Seq[String]
             )(implicit logger: SelfAwareStructuredLogger[IO],
               implicit val collector: CollectorRegistry) {

  val allLabels = Seq("method", "result") ++ labelNames
  val metricName = scope.replaceAll("-", "_")

  val counter = Counter.build.name(s"{$metricName}_success").labelNames(allLabels: _*).help(s"{$metricName}_success").register(collector)

  val histogram = Histogram.build.name(s"{$metricName}_execution_time").labelNames(allLabels: _*).help(s"{$metricName}_execution_time").register

  def monitor[T](method: String, labelValues: Seq[String], context: Map[String, String])(monad: () => IO[T]): IO[T] = {

    val timer = histogram.startTimer()

    val io = for {
      _ <- logger.info(context)(s"{$scope}.{$method} started")
      result <- monad()
      _ <- IO.defer {
        val actualLabelValues = Seq(method, "success") ++ labelValues
        counter.labels(actualLabelValues: _*).inc()
        histogram.labels(actualLabelValues: _*) .observe(timer.observeDuration())
        IO.unit
      }
      _ <- logger.info(context)(s"{$scope}.{$method} success")
    } yield result

    io.onError(throwable => {
      for {
        _ <- IO.defer{
          counter.labels(Seq(method, "error") ++ labelValues: _*).inc()
          IO.unit
        }
        _ <- logger.error(context, throwable)(s"{$scope}.{$method} error")
      } yield ()
    })
  }
}
