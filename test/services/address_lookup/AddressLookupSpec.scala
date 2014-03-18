package services.address_lookup

import services.address_lookup.ordnance_survey.AddressLookupServiceImpl
import services.fakes.FakeWebServiceImpl
import helpers.UnitSpec
import helpers.disposal_of_vehicle.Helper._

class AddressLookupSpec extends UnitSpec {
  "postcodeWithNoSpaces" should {
    val addressLookupService = new AddressLookupServiceImpl(ws = new FakeWebServiceImpl)

    "return the same string if no spaces present" in {
      val result = addressLookupService.postcodeWithNoSpaces(postcodeValid)

      result should equal(postcodeValid)
    }

    "remove spaces when present" in {
      val result = addressLookupService.postcodeWithNoSpaces(postcodeValidWithSpace)

      result should equal(postcodeValid)
    }
  }
}
