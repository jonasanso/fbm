package nlp.fbm

import Math._

import breeze.linalg.DenseVector
import breeze.math.Complex
import breeze.signal._
import breeze.stats.distributions.Gaussian
import eu.timepit.refined.auto._

case class FBM(n: N, hurst: Hurst, length: N = 1) {
  private val pureRandom: Hurst = 0.5
  private lazy val eigenvals = FBM.eigenvals(n, hurst)

  def fgn(gn: Vector[Double] = randomNormal(), fn: Vector[Double] => Vector[Double] = this.daviesharte(randomNormal())): Vector[Double] = {
    val scale = (1.0 * length / n) ** hurst

    (if (hurst == pureRandom) gn else fn(gn)) * scale
  }

  /**
   * Generate a fgn realization using Davies-Harte method.
   * Uses Davies and Harte method (exact method) from:
   * Davies, Robert B., and D. S. Harte. "Tests for Hurst effect."
   * Biometrika 74, no. 1 (1987): 95-101.
   * Can fail if n is small and hurst close to 1. Falls back to Hosking
   * method in that case. See:
   * Wood, Andrew TA, and Grace Chan. "Simulation of stationary Gaussian
   * processes in [0, 1] d." Journal of computational and graphical
   * statistics 3, no. 4 (1994): 409-432.
   */
  private[fbm] def daviesharte(gn2: Vector[Double])(gn: Vector[Double]): Vector[Double] = {
    if (eigenvals.exists(_ < 0)) ??? // TODO: Implement Hosking
    val row = FBM.fgnRow(gn, gn2, eigenvals)
    fourierTr(DenseVector(row.toArray)).map(_.real).toScalaVector().take(n)
  }

  private def randomNormal(): Vector[Double] = {
    Gaussian(mu = 0.0, sigma = 1.0).sample(n).toVector
  }


}

object FBM {
  def autocovariance(hurst: Hurst)(k: Int): Double =
    0.5 * (abs(k - 1) ** (2 * hurst) - 2 * (abs(k) ** (2 * hurst)) + abs(k + 1) ** (2 * hurst))

  def eigenvals(n: N, hurst: Hurst): Vector[Double] = {
    def autocov: Int => Double = autocovariance(hurst)

    val rowComponent = 1.until(n).map(autocov).toVector
    val row: Vector[Double] = (autocov(0) +: rowComponent :+ 0d) ++ rowComponent.reverse
    fourierTr(DenseVector(row.toArray)).map(_.real).toScalaVector()
  }

  def fgnRow(gn: Vector[Double], gn2: Vector[Double], eigenvals: Vector[Double]): Vector[Complex] = {
    val n = gn.size
    0.until(2 * n).toVector map {
      case i if i == 0 => Complex(sqrt(eigenvals(i) / (2 * n)) * gn(i), 0)
      case i if i < n => Complex(sqrt(eigenvals(i) / (4 * n)), 0) * Complex(gn(i), gn2(i))
      case i if i == n => Complex(sqrt(eigenvals(i) / (2 * n)) * gn2(0), 0)
      case i if i > n => Complex(sqrt(eigenvals(i) / (4 * n)), 0) * Complex(gn(2 * n - i), -1 * gn2(2 * n - i))
    }
  }
}
