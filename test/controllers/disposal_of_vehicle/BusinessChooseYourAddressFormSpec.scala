package controllers.disposal_of_vehicle

import helpers.UnitSpec
import mappings.disposal_of_vehicle.BusinessChooseYourAddress._
import services.fakes.FakeAddressLookupWebServiceImpl
import services.fakes.FakeAddressLookupWebServiceImpl._
import composition.TestComposition.{testInjector => injector}
import common.ClientSideSessionFactory

class BusinessChooseYourAddressFormSpec extends UnitSpec {
  "form" should {
    "accept when all fields contain valid responses" in {
      formWithValidDefaults().get.uprnSelected should equal(traderUprnValid.toString)
    }
  }

  "addressSelect" should {
    "reject if empty" in {
      val errors = formWithValidDefaults(addressSelected = "").errors
      errors.length should equal(1)
      errors(0).key should equal(AddressSelectId)
      errors(0).message should equal("error.required")
    }
  }

  private def businessChooseYourAddressWithFakeWebService(uprnFound: Boolean = true) = {
    val responsePostcode = if(uprnFound) responseValidForPostcodeToAddress else responseValidForPostcodeToAddressNotFound
    val responseUprn = if(uprnFound) responseValidForUprnToAddress else responseValidForUprnToAddressNotFound
    val fakeWebService = new FakeAddressLookupWebServiceImpl(responsePostcode, responseUprn)
    val addressLookupService = new services.address_lookup.ordnance_survey.AddressLookupServiceImpl(fakeWebService)
    val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
    new BusinessChooseYourAddress(addressLookupService)(clientSideSessionFactory)
  }

  private def formWithValidDefaults(addressSelected: String = traderUprnValid.toString) = {
    businessChooseYourAddressWithFakeWebService().form.bind(
      Map(AddressSelectId -> addressSelected)
    )
  }
}
