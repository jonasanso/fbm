package terminal

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.Try
import scala.concurrent.blocking


object ConsoleInput {

  case class ConsoleInputResult(value: Boolean, next: ConsoleInput)

  class ConsoleInput(f: Future[Unit]) {

    def waitForInput(timeoutMillis: Duration): ConsoleInputResult = {
      Try(Await.result(f, timeoutMillis)).toOption
        .map(_ => ConsoleInputResult(value = true, next()))
        .getOrElse(ConsoleInputResult(value = false, this))
    }
  }

  private var status: ConsoleInput = next()

  def waitForInput(timeoutMillis: Duration): Boolean = {
    val result = status.waitForInput(timeoutMillis)
    status = result.next
    result.value
  }

  private def next() = new ConsoleInput(readCharF())

  def readCharF(): Future[Unit] =
    Future {
      blocking {
        System.in.read()
      }
    }

}
