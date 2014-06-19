package helpers.common

import org.scalatest.{Matchers, WordSpec}

class RandomVrmGeneratorTest  extends WordSpec with Matchers {

  "Random vrm generator" should {
    "not generate the same vrm twice" in {
      val first = RandomVrmGenerator.vrm
      val second = RandomVrmGenerator.vrm
      first should not equal(second)
    }

  }
}