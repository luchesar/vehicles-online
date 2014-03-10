package models.domain.common

import org.scalatest.{Matchers, WordSpec}

class AddressAndPostcodeSpec extends WordSpec with Matchers {
  "Address - model" should {
    "return expected toString value" in {
      val address = AddressAndPostcodeModel(addressLinesModel = AddressLinesModel(line1 = "a",
        line2 = Some("b"),
        line3 = Some("c"),
        line4 = Some("d")),
        postcode = "e")

      val result = address.toViewFormat.mkString(", ")

      result should equal("a, b, c, d, e")
    }

    "return expected toString value with missings values" in {
      val address = AddressAndPostcodeModel(addressLinesModel = AddressLinesModel(line1 = "a",
        line2 = None,
        line3 = None,
        line4 = None),
        postcode = "e")

      val result = address.toViewFormat.mkString(", ")

      result should equal("a, e")
    }
  }
}