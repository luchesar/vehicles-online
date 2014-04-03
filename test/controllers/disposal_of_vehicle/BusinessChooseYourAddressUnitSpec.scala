package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import mappings.disposal_of_vehicle.BusinessChooseYourAddress._
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup
import services.fakes.{FakeAddressLookupService, FakeWebServiceImpl}
import helpers.UnitSpec
import services.fakes.FakeWebServiceImpl._

class BusinessChooseYourAddressUnitSpec extends UnitSpec {
  "BusinessChooseYourAddress - Controller" should {
    def businessChooseYourAddressWithFakeWebService(uprnFound: Boolean = true) = {
      val response = if(uprnFound) responseValidForOrdnanceSurvey else responseValidForOrdnanceSurveyNotFound
      val fakeWebService = new FakeWebServiceImpl(response, response)
      val addressLookupService = new services.address_lookup.ordnance_survey.AddressLookupServiceImpl(fakeWebService)

      new BusinessChooseYourAddress(addressLookupService)
    }

    "present" in new WithApplication {
      CacheSetup.setupTradeDetails()
      val request = FakeRequest().withSession()
      val businessChooseYourAddress = businessChooseYourAddressWithFakeWebService()

      val result = businessChooseYourAddress.present(request)

      status(result) should equal(OK)
    }

    "redirect to VehicleLookup page after a valid submit" in new WithApplication {
      CacheSetup.setupTradeDetails()
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        addressSelectId -> uprnValid.toString)
      val businessChooseYourAddress = businessChooseYourAddressWithFakeWebService()

      val result = businessChooseYourAddress.submit(request)

      redirectLocation(result) should equal(Some(VehicleLookupPage.address))
    }

    "return a bad request after no submission" in new WithApplication {
      CacheSetup.setupTradeDetails()
      val request = FakeRequest().withSession().withFormUrlEncodedBody()
      val businessChooseYourAddress = businessChooseYourAddressWithFakeWebService()

      val result = businessChooseYourAddress.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request after a blank submission" in new WithApplication {
      CacheSetup.setupTradeDetails()
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        addressSelectId -> "")
      val businessChooseYourAddress = businessChooseYourAddressWithFakeWebService()

      val result = businessChooseYourAddress.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "redirect to setupTradeDetails page when present with no dealer name cached" in new WithApplication {
      val request = FakeRequest().withSession()
      val businessChooseYourAddress = businessChooseYourAddressWithFakeWebService()

      val result = businessChooseYourAddress.present(request)

      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.address))

    }

    "redirect to setupTradeDetails page when valid submit with no dealer name cached" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        addressSelectId -> uprnValid.toString)
      val businessChooseYourAddress = businessChooseYourAddressWithFakeWebService()

      val result = businessChooseYourAddress.submit(request)

      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.address))
    }

    "redirect to setupTradeDetails page when bad submit with no dealer name cached" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        addressSelectId -> "")
      val businessChooseYourAddress = businessChooseYourAddressWithFakeWebService()

      val result = businessChooseYourAddress.submit(request)

      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.address))
    }

    "redirect to UprnNotFound page when Uprn returns no match on submit" in new WithApplication {
      CacheSetup.setupTradeDetails()
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        addressSelectId -> FakeAddressLookupService.uprnInvalid.toString)
      val businessChooseYourAddress = businessChooseYourAddressWithFakeWebService(uprnFound = false)

      val result = businessChooseYourAddress.submit(request)

      redirectLocation(result) should equal(Some(UprnNotFoundPage.address))
    }
  }
}
