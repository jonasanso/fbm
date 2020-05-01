package nlp.fbm
import BasicMath._

object Main {

  def main(args: Array[String]): Unit = {
    Opts.parser.parse(args, Opts.default).foreach(run)
  }

  private def run(opts: Opts): Unit = {
    def history(init: Double): Vector[Double] = {
      FBM(opts.n, opts.hurst, opts.length).fgn()
        .foldLeft(Vector(init)) { (acc, d) =>
          val next = if (acc.last > 0) truncate(acc.last + d) else 0
          acc.appended(next)
        }
    }

    val segment1 = history(opts.wealth)
    val segment2 = history(segment1.last)
    val segment3 = history(segment2.last)
    val segment4 = history(segment3.last)

    val series = segment1 ++ segment2 ++ segment3 ++ segment4
    for {
      w <- series
    } {
      println(s"""{"wealth": $w}""")
      Thread.sleep(100)
    }
    // Give some time to jplot to render the results
    Thread.sleep(3000)
  }
}


object BasicMath {
  def truncate(n: Double): Double = {
    math.floor(n * 100) / 100 match {
      case Double.PositiveInfinity => Double.MaxValue
      case x if x > 0 => x
      case _ => 0
    }
  }

}


