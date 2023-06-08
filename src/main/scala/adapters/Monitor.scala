package adapters

import cats.effect.IO
import io.prometheus.client.{CollectorRegistry, Counter, Histogram}
import org.typelevel.log4cats.SelfAwareStructuredLogger

class Monitor(
               scope: String,
               labelNames: Seq[String]
             )(implicit logger: SelfAwareStructuredLogger[IO],
               implicit val collector: CollectorRegistry) {

  private val allLabels = Seq("method", "result") ++ labelNames
  private val metricName = scope.replaceAll("-", "_")

  private val counter = Counter.build.name(f"$metricName%s_success").labelNames(allLabels: _*).help(f"$metricName%s_success").register(collector)

  private val histogram = Histogram.build.name(f"$metricName%s_execution_time").labelNames(allLabels: _*).help(f"$metricName%s_execution_time").register(collector)

  def monitor[T](method: String, labelValues: Seq[String], context: Map[String, String])(monad: () => IO[T]): IO[T] = {

    val actualLabelValues = Seq(method, "success") ++ labelValues
    val timer = histogram.labels(actualLabelValues: _*).startTimer()

    val io = for {
      _ <- logger.info(context)(s"$scope.$method started")
      result <- monad()
      _ <- IO.defer {

        counter.labels(actualLabelValues: _*).inc()
        histogram.labels(actualLabelValues: _*) .observe(timer.observeDuration())
        IO.unit
      }
      _ <- logger.info(context)(s"$scope.$method success")
    } yield result

    io.onError(throwable => {
      for {
        _ <- IO.defer{
          counter.labels(Seq(method, "error") ++ labelValues: _*).inc()
          IO.unit
        }
        _ <- logger.error(context, throwable)(s"$scope.$method error")
      } yield ()
    })
  }
}
