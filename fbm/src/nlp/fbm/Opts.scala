package nlp.fbm

import eu.timepit.refined.scopt._
import eu.timepit.refined.auto._

case class Opts(wealth: Double, numSegments: N, n: N, hurst: Hurst, lengthOpt: Option[N]) {
  val length = lengthOpt.getOrElse(n)
}

object Opts {
  private val INITIAL_VALUE = 100d
  private val NUM_SEGMENTS: N = 10
  private val FOUR_YEAR_CYCLE: N = 365 * 4
  private val DEFAULT_HURST: Hurst = 0.68

  val default = Opts(INITIAL_VALUE, NUM_SEGMENTS, FOUR_YEAR_CYCLE, DEFAULT_HURST, None)
  val parser = new scopt.OptionParser[Opts]("fbm") {
    head("Generate fractal gaussian time series")

    opt[Double]('w', "wealth")
      .action((x, c) => c.copy(wealth = x))
      .text("initial wealth")

    opt[N]('s', "segments")
      .action((x, c) => c.copy(numSegments = x))
      .text("number of different segments")

    opt[N]('n', "num")
      .action((x, c) => c.copy(n = x))
      .text("num is the number of samples")

    opt[Hurst]('h', "hurst")
      .action((h, c) => c.copy(hurst = h))
      .text("Hurst is a Double between 0 and 1 is the hurst exponent for the fractal")

    opt[N]('l', "length")
      .action((l, c) => c.copy(lengthOpt = Some(l)))
      .text("length is used to define the scale. For scale 1 length is equal to the number of samples")
  }
}
