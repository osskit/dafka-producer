package ports

import cats.effect.IO

trait Producer {
  def produce(request: ProduceRequest) : IO[Unit]
  def healthy() : IO[Boolean]
}
