package nlp.fbm

import nlp.fbm.FixedSize._
import utest._

object SizedVectorTest extends TestSuite {

  val tests: Tests = Tests {
    "fixed multiply" - {
      FixedSize.multiply(initial, 2.0) ==> doubled
    }

    "Dynamic multiply" - {
      val dynamic = DynamicFixedSize(2)
      dynamic.multiply(dynamic.range, 2.0).value ==> Vector(2.0, 4.0)
    }
  }

}
