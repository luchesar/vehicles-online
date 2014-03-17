package services.address_lookup.ordnance_survey

import helpers.UnitSpec

class WebServiceImplSpec extends UnitSpec {
  "postcodeWithNoSpaces" should {
    import helpers.disposal_of_vehicle.PostcodePage.{postcodeValid, postcodeValidWithSpace}
    val addressLookupService = new services.address_lookup.ordnance_survey.WebServiceImpl()

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
