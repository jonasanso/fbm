package nlp.fbm

import eu.timepit.refined._
import eu.timepit.refined.api._
import eu.timepit.refined.auto._
import eu.timepit.refined.collection._
import eu.timepit.refined.generic._
import shapeless.Witness


object FixedSize {
  type Sized = Size[Equal[W.`2`.T]]
  type DoublesFixedSize = Vector[Double] Refined Size[Equal[W.`2`.T]]

  def multiply(v: DoublesFixedSize, d: Double): DoublesFixedSize = refineV[Sized].unsafeFrom(v.map(_ * d))

  val initial: DoublesFixedSize = refineV[Sized].unsafeFrom(Vector(1.0, 2.0))
  val doubled: DoublesFixedSize = refineV[Sized].unsafeFrom(Vector(2.0, 4.0))

}

case class DynamicFixedSize(n: Int) {
  private val sizeWitness = Witness(n)
  private type S = sizeWitness.T
  type Sized = Size[Equal[S]]

  type DoublesDynamicFixedSize = Vector[Double] Refined Sized

  def multiply(v: DoublesDynamicFixedSize, d: Double): DoublesDynamicFixedSize = refineV[Sized].unsafeFrom(v.map(_ * d))

  def range: DoublesDynamicFixedSize = refineV[Sized].unsafeFrom(1.to(n).map(_.toDouble).toVector)
}

