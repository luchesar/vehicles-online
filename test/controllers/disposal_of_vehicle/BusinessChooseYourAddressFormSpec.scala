package controllers.disposal_of_vehicle

import mappings.disposal_of_vehicle.BusinessChooseYourAddress._
import services.fakes.FakeWebServiceImpl
import helpers.UnitSpec
import services.fakes.FakeWebServiceImpl._

class BusinessChooseYourAddressFormSpec extends UnitSpec {
  "addressSelect" should {
    def businessChooseYourAddressWithFakeWebService(uprnFound: Boolean = true) = {
      val response = if(uprnFound) responseValidForOrdnanceSurvey else responseValidForOrdnanceSurveyNotFound
      val fakeWebService = new FakeWebServiceImpl(response, response)
      val addressLookupService = new services.address_lookup.ordnance_survey.AddressLookupServiceImpl(fakeWebService)
      new BusinessChooseYourAddress(addressLookupService)
    }

    def formWithValidDefaults(addressSelected: String = traderUprnValid.toString) = {
      businessChooseYourAddressWithFakeWebService().form.bind(
        Map(addressSelectId -> addressSelected)
      )
    }

    "accept if valid" in {
      formWithValidDefaults().get.uprnSelected should equal(traderUprnValid)
    }

    "reject if empty" in {
      val errors = formWithValidDefaults(addressSelected = "").errors
      errors.length should equal(1)
      errors(0).key should equal(addressSelectId)
      errors(0).message should equal("error.number")
    }
  }
}
