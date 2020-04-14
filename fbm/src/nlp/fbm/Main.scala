package nlp.fbm

import eu.timepit.refined.auto._

object Main {
  def main(args: Array[String]): Unit = {
    val opts = new Opts(args)

    for {
      d <- FBM(100, 0.68, 1000).fgn()
    } {
      println(s"""{"value": $d}""")
      Thread.sleep(100)
    }
  }

  // Give some timo to jplot to render the results
  Thread.sleep(3000)
}





