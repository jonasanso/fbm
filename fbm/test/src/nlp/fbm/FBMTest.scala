package nlp.fbm

import breeze.math.Complex
import utest._
import nlp.fbm.FBM._
import eu.timepit.refined.auto._

object FBMTest extends utest.TestSuite {
  val gn = Vector(1.1914656899846945, -0.3988215313600739)
  val gn2 = Vector(-0.33874629706080445, 2.1631714672672033)
  val eigen = Vector(1.8284271247461903, 1.0, 0.1715728752538097, 1.0)

  val tests: Tests = Tests {
    "fgn should scale random gn when Hurst is 0.5" - {
      FBM(1, 0.5, 1).fgn(gn, _ => ???) ==> gn
      FBM(1, 0.5, 10).fgn(gn, _ => ???) ==> gn.map(_ * Math.pow(10, 0.5))
    }

    "fgn should scale random gn when Hurst is not 0.5" - {
      val fgn = Vector(-0.1715728752538097, 0.8284271247461903)
      FBM(1, 0.1, 1).fgn(gn, _ => fgn) ==> fgn
      FBM(1, 1.0, 10).fgn(gn, _ => fgn) ==> fgn.map(_ * 10)
      FBM(1, 1.0, 100).fgn(gn, _ => fgn) ==> fgn.map(_ * 100)
      FBM(2, 0.68, 100).fgn(gn, _ => fgn) ==> fgn.map(_ * Math.pow(50, 0.68))
    }

    "autocovariance" - {
      autocovariance(0.75)(0) ==> 1
      autocovariance(0.75)(1) ==> 0.41421356237309515
    }

    "eigenvals" - {
      eigenvals(2, 0.75) ==> eigen
      eigenvals(3, 0.75) ==> Vector(2.3677252979604417, 1.1445644757659694, 0.31613735101977913, 0.7108710484680612, 0.31613735101977913, 1.1445644757659694)
    }

    "fgnRow" - {
      fgnRow(gn, gn2, eigen) ==>
      Vector(Complex(0.8055460506232213, 0), Complex(-0.14100470465395581, 0.7647966066869467), Complex(-0.07015665522312521, 0), Complex(-0.14100470465395581, -0.7647966066869467))
    }

    "daviesharte" - {
      FBM(2, 0.75).daviesharte(gn2)(gn) ==>
        Vector(0.45337998609218444, 2.40529591922024)
    }

  }
}
