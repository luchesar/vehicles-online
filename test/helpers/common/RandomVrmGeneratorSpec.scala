package helpers.common

import helpers.UnitSpec

final class RandomVrmGeneratorSpec extends UnitSpec {

  "Random vrm generator" should {
    "not generate the same vrm twice" in {
      val first = RandomVrmGenerator.vrm
      val second = RandomVrmGenerator.vrm
      first should not equal second
    }

  }
}