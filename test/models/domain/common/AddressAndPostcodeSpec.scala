package models.domain.common

import org.scalatest.{Matchers, WordSpec}

final class AddressAndPostcodeSpec extends WordSpec with Matchers {
  "Address - model" should {
    "return expected toString value" in {
      val address = AddressAndPostcodeModel(addressLinesModel = AddressLinesModel(buildingNameOrNumber = "abcd",
        line2 = Some("e"),
        line3 = Some("f"),
        postTown = "ghi"),
        postcode = "j")

      val result = address.toViewFormat.mkString(", ")

      result should equal("ABCD, E, F, GHI, J")
    }

    "return expected toString value with missings values" in {
      val address = AddressAndPostcodeModel(addressLinesModel = AddressLinesModel(buildingNameOrNumber = "abcd",
        line2 = None,
        line3 = None,
        postTown = "efg"),
        postcode = "h")

      val result = address.toViewFormat.mkString(", ")

      result should equal("ABCD, EFG, H")
    }
  }
}