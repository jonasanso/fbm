package nlp.fbm


object BasicMath {

  def accumulate(i: Double, d: Double): Double =
    if (i > 0) truncate(i + d) else 0

  def truncate(n: Double): Double = {
    math.floor(n * 100) / 100 match {
      case Double.PositiveInfinity => Double.MaxValue
      case x if x > 0 => x
      case _ => 0
    }
  }

}


