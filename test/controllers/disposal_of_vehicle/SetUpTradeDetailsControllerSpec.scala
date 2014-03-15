package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import mappings.disposal_of_vehicle.SetupTradeDetails._
import org.scalatest.{Matchers, WordSpec}
import helpers.disposal_of_vehicle.BusinessChooseYourAddressPage
import helpers.disposal_of_vehicle.Helper._

class SetUpTradeDetailsControllerSpec extends WordSpec with Matchers {
  "BeforeYouStart - Controller" should {

    "present" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.SetUpTradeDetails.present(request)

      status(result) should equal(OK)
    }

    "redirect to next page when the form is completed successfully" in new WithApplication {
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(dealerNameId -> traderBusinessNameValid, dealerPostcodeId -> traderPostcodeValid)

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some(BusinessChooseYourAddressPage.url))
    }

    "return a bad request when only dealerName is entered" in new WithApplication {
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(dealerNameId -> traderBusinessNameValid)

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request when only traderPostcode is entered" in new WithApplication {
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(dealerPostcodeId -> traderPostcodeValid)

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request when empty strings are entered" in new WithApplication {
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(dealerNameId -> "", dealerPostcodeId -> "")

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request when a postcode containing special characters is entered" in new WithApplication {
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(dealerNameId -> "", dealerPostcodeId -> "SA99 1D£")

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request when a postcode with a length more than max length is entered" in new WithApplication {
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(dealerNameId -> "", dealerPostcodeId -> "SA99 1DDD")

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request when a postcode with a length less than min length is entered" in new WithApplication {
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(dealerNameId -> "", dealerPostcodeId -> "SA99")

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request when a postcode with an incorrect format is entered" in new WithApplication {
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(dealerNameId -> "", dealerPostcodeId -> "9A3F2")

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      status(result) should equal(BAD_REQUEST)
    }


    "return a bad request if no details are entered" in new WithApplication {
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      status(result) should equal(BAD_REQUEST)
    }
  }
}