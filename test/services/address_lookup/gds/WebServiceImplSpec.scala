package services.address_lookup.gds

import helpers.UnitSpec
import services.fakes.FakeAddressLookupService._
import utils.helpers.Config

final class WebServiceImplSpec extends UnitSpec {
  "postcodeWithNoSpaces" should {
    "return the same string if no spaces present" in {
      val result = addressLookupService.postcodeWithNoSpaces(postcodeValid)

      result should equal(postcodeValid)
    }

    "remove spaces when present" in {
      val result = addressLookupService.postcodeWithNoSpaces(postcodeValidWithSpace)

      result should equal(postcodeValid)
    }
  }

  private val addressLookupService = new services.address_lookup.gds.WebServiceImpl(new Config())
}
