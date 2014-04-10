package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import mappings.disposal_of_vehicle.SetupTradeDetails._
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.Helper._
import helpers.UnitSpec
import services.fakes.FakeAddressLookupService._

class SetUpTradeDetailsUnitSpec extends UnitSpec {
  private def buildCorrectlyPopulatedRequest(dealerName: String = traderBusinessNameValid.toString, dealerPostcode: String = postcodeValid) = {
    FakeRequest().withSession().withFormUrlEncodedBody(
      dealerNameId -> dealerName,
      dealerPostcodeId -> dealerPostcode)
  }

  "BeforeYouStart - Controller" should {
    "present" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = disposal_of_vehicle.SetUpTradeDetails.present(request)
      status(result) should equal(OK)
    }

    "redirect to next page when the form is completed successfully" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest()
      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)
      redirectLocation(result) should equal (Some(BusinessChooseYourAddressPage.address))
    }

    "return a bad request if no details are entered" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest(dealerName = "", dealerPostcode = "")
      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)
      status(result) should equal(BAD_REQUEST)
    }

    "replace max length error message for traderBusinessName with standard error message (US158)" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest(dealerName = "a" * (dealerNameMaxLength + 1))
      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)
      val count = countSubstring(contentAsString(result), "Must be between two and 30 characters and not contain invalid characters")
      count should equal(2)
    }

    "replace required and min length error messages for traderBusinessName with standard error message (US158)" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest(dealerName = "")
      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)
      val count = countSubstring(contentAsString(result), "Must be between two and 30 characters and not contain invalid characters")
      count should equal(2) // The same message is displayed in 2 places - once in the validation-summary at the top of
      // the page and once above the field.
    }
  }
}