package nlp.fbm

import eu.timepit.refined.scopt._
import eu.timepit.refined.auto._

case class Opts(n: N, hurst: Hurst, lengthOpt: Option[N]) {
  val length = lengthOpt.getOrElse(n)
}

object Opts {
  val default = Opts(100000, 0.68, None)
  val parser = new scopt.OptionParser[Opts]("fbm") {
    head("Generate fractal gaussian time series")

    opt[N]('n', "num")
      .action((x, c) => c.copy(n = x))
      .text("num is the number of samples")

    opt[Hurst]('h', "hurst")
      .action((h, c) => c.copy(hurst = h))
      .text("Hurst is a Double between 0 and 1 is the hurst exponent for the fractal")

    opt[N]('l', "Lenght")
      .action((l, c) => c.copy(lengthOpt = Some(l)))
      .text("length is used to define the scale. For scale 1 length is equal to the number of samples")
  }
}
