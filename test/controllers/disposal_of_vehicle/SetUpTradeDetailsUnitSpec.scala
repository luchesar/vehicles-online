package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import mappings.disposal_of_vehicle.SetupTradeDetails._
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.Helper._
import helpers.UnitSpec

class SetUpTradeDetailsUnitSpec extends UnitSpec {
  "BeforeYouStart - Controller" should {

    "present" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.SetUpTradeDetails.present(request)

      status(result) should equal(OK)
    }

    "redirect to next page when the form is completed successfully" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        dealerNameId -> traderBusinessNameValid,
        dealerPostcodeId -> postcodeValid)

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      redirectLocation(result) should equal (Some(BusinessChooseYourAddressPage.address))
    }

    "return a bad request when only dealerName is entered" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        dealerNameId -> traderBusinessNameValid)

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request when a trader name is entered containing only punctuation is entered" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        dealerNameId -> "...",
        dealerPostcodeId -> postcodeValid)

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request when only traderPostcode is entered" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        dealerPostcodeId -> postcodeValid)

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request when dealer name is empty" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        dealerNameId -> "",
        dealerPostcodeId -> postcodeValid)

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request when postcode is empty" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        dealerNameId -> traderBusinessNameValid,
        dealerPostcodeId -> "")

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request when a postcode containing special characters is entered" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        dealerNameId -> traderBusinessNameValid,
        dealerPostcodeId -> "SA99 1D£")

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request when a postcode with a length more than max length is entered" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        dealerNameId -> traderBusinessNameValid,
        dealerPostcodeId -> "SA99 1DDD")

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request when a postcode with a length less than min length is entered" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        dealerNameId -> traderBusinessNameValid,
        dealerPostcodeId -> "SA99")

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request when a postcode with an incorrect format is entered" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        dealerNameId -> traderBusinessNameValid,
        dealerPostcodeId -> "9A3F2")

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      status(result) should equal(BAD_REQUEST)
    }


    "return a bad request if no details are entered" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody()

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request when a dealer name is entered with to many characters" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        dealerNameId -> ("a" * 31),
        dealerPostcodeId -> postcodeValid)

      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      status(result) should equal(BAD_REQUEST)
    }
  }
}