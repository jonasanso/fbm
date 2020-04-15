package nlp.fbm

object Main {
  def main(args: Array[String]): Unit = {
    Opts.parser.parse(args, Opts.default).foreach(run)
  }

  private def run(opts: Opts) = {
    def doubles(): Vector[Double] = FBM(opts.n, opts.hurst, opts.length).fgn()

    val series = Vector.fill(5)(doubles()).transpose
    for {
      Vector(a1, a2, a3, a4, a5) <- series
    } {
      println(s"""{"series1": $a1,"series2": $a2,"series3": $a3,"series4": $a4,"series5": $a5}""")
      Thread.sleep(100)
    }
    // Give some timo to jplot to render the results
    Thread.sleep(3000)
  }
}





