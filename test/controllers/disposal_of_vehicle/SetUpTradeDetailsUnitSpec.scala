package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import mappings.disposal_of_vehicle.SetupTradeDetails._
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.Helper._
import helpers.UnitSpec
import services.fakes.FakeAddressLookupService._
import services.session.PlaySessionState

class SetUpTradeDetailsUnitSpec extends UnitSpec {

  "BeforeYouStart - Controller" should {

    "present" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = setUpTradeDetails(newSessionState).present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to next page when the form is completed successfully" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest()
      val result = setUpTradeDetails(newSessionState).submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(BusinessChooseYourAddressPage.address))
      }
    }

    "return a bad request if no details are entered" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest(dealerName = "", dealerPostcode = "")
      val result = setUpTradeDetails(newSessionState).submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "replace max length error message for traderBusinessName with standard error message (US158)" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest(dealerName = "a" * (dealerNameMaxLength + 1))
      val result = setUpTradeDetails(newSessionState).submit(request)
      val count = countSubstring(contentAsString(result), "Must be between two and 30 characters and not contain invalid characters")
      count should equal(2)
    }

    "replace required and min length error messages for traderBusinessName with standard error message (US158)" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest(dealerName = "")
      val result = setUpTradeDetails(newSessionState).submit(request)
      val count = countSubstring(contentAsString(result), "Must be between two and 30 characters and not contain invalid characters")
      count should equal(2) // The same message is displayed in 2 places - once in the validation-summary at the top of
      // the page and once above the field.
    }
  }

  private def buildCorrectlyPopulatedRequest(dealerName: String = traderBusinessNameValid.toString, dealerPostcode: String = postcodeValid) = {
    FakeRequest().withSession().withFormUrlEncodedBody(
      dealerNameId -> dealerName,
      dealerPostcodeId -> dealerPostcode)
  }

  private def setUpTradeDetails(sessionState: DisposalOfVehicleSessionState) =
    new SetUpTradeDetails(sessionState)

  private def newSessionState = {
    val sessionState = new PlaySessionState()
    new DisposalOfVehicleSessionState(sessionState)
  }
}
