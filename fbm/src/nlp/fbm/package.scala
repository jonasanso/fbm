package nlp

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.boolean._
import eu.timepit.refined.numeric._

package object fbm {
  type N = Int Refined Positive
  type ZeroToOne = Not[Less[0.0]] And Not[Greater[1.0]]
  type Hurst = Double Refined ZeroToOne

  implicit class DoubleOps(a: Double) {
    def **(b: Double): Double = Math.pow(a, b)
  }

  implicit class VectorOps(s: Vector[Double]) {
    def *(b: Double): Vector[Double] = s.map(_ * b)
  }


}
