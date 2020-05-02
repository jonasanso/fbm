package nlp.fbm

import BasicMath._
import eu.timepit.refined.auto._
import terminal.ConsoleInput
import scala.concurrent.duration._

object Main {

  def main(args: Array[String]): Unit = {
    Opts.parser.parse(args, Opts.default).foreach(run)
  }

  private def run(opts: Opts): Unit = {
    def fgn(): Vector[Double] = FBM(opts.n, opts.hurst, opts.length).fgn()

    def history(init: Double, fbm: Vector[Double]): Vector[Double] = {
      fbm.foldLeft(Vector(init)) { (acc, d) =>
        acc.appended(accumulate(acc.last, d))
      }
    }

    for {
      w <- history(opts.wealth, Vector.fill(opts.numSegments)(fgn()).flatten)
    } {
      println(s"""{"wealth": $w}""")
      if (ConsoleInput.waitForInput(100.millis)) ConsoleInput.waitForInput(Duration.Inf)
    }
    // Give some time to jplot to render the results
    Thread.sleep(3000)
  }
}


