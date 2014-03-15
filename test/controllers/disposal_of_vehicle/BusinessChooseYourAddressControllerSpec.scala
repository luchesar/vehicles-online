package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import helpers.disposal_of_vehicle.{SetUpTradeDetailsPage, VehicleLookupPage, UprnNotFoundPage}
import mappings.disposal_of_vehicle.BusinessChooseYourAddress._
import services.fakes.{FakeAddressLookupService, FakeWebServiceImpl}
import helpers.UnitSpec

class BusinessChooseYourAddressUnitSpec extends UnitSpec {

  "BusinessChooseYourAddress - Controller" should {
    val fakeWebService = new FakeWebServiceImpl()
    val fakeAddressLookupService = new FakeAddressLookupService(fakeWebService)
    val businessChooseYourAddress = new BusinessChooseYourAddress(fakeAddressLookupService)

    "present" in new WithApplication {
      SetUpTradeDetailsPage.setupCache()
      val request = FakeRequest().withSession()

      val result = businessChooseYourAddress.present(request)

      status(result) should equal(OK)
    }

    "redirect to VehicleLookup page after a valid submit" in new WithApplication {
      SetUpTradeDetailsPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(addressSelectId -> "1234")

      val result = businessChooseYourAddress.submit(request)

      redirectLocation(result) should equal(Some(VehicleLookupPage.url))
    }

    "return a bad request after no submission" in new WithApplication {
      SetUpTradeDetailsPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      val result = businessChooseYourAddress.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request after a blank submission" in new WithApplication {
      SetUpTradeDetailsPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(addressSelectId -> "")

      val result = businessChooseYourAddress.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "redirect to setupTradeDetails page when present with no dealer name cached" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = businessChooseYourAddress.present(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to setupTradeDetails page when valid submit with no dealer name cached" in new WithApplication {
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(addressSelectId -> "1234")

      val result = businessChooseYourAddress.submit(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to setupTradeDetails page when bad submit with no dealer name cached" in new WithApplication {
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(addressSelectId -> "")

      val result = businessChooseYourAddress.submit(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to UprnNotFound page when Uprn returns no match on submit" in new WithApplication {
      SetUpTradeDetailsPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(addressSelectId -> "9999")

      val result = businessChooseYourAddress.submit(request)

      redirectLocation(result) should equal(Some(UprnNotFoundPage.url))
    }
  }
}
