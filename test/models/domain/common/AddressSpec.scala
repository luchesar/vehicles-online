package models.domain.common

import org.scalatest.{Matchers, WordSpec}

class AddressSpec extends WordSpec with Matchers {
  "Address - model" should {
    "return expected toString value" in {
      val address = Address(line1 = "a", line2 = Some("b"), line3 = Some("c"), line4 = Some("d"), postCode = "e")

      val result = address.toString()

      result should equal("a, b, c, d, e")
    }

    "return expected toString value with missings values" in {
      val address = Address(line1 = "a", postCode = "e")

      val result = address.toString()

      result should equal("a, , , , e")
    }
  }
}