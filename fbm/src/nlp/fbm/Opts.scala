package nlp.fbm

import org.rogach.scallop.ScallopConf

class Opts(args: Array[String]) extends ScallopConf(args) {
  val ver = opt[String]("version", default = Some("1.0"))

  verify()
}
