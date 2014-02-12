package controllers.disposal_of_vehicle

import mappings.disposal_of_vehicle.SetupTradeDetails._
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import mappings.disposal_of_vehicle.SetupTradeDetails._
import org.scalatest.{Matchers, WordSpec}


class SetUpTradeDetailsControllerSpec extends WordSpec with Matchers {

  "BeforeYouStart - Controller" should {

    "present" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.SetUpTradeDetails.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to next page after the look-up is clicked" in new WithApplication {
      // Arrange
      val traderBusinessNameValid = "Example Trader Name"
      val traderPostcodeValid = "SA99 1BD"

      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(dealerNameId -> traderBusinessNameValid, dealerPostcodeId -> traderPostcodeValid)

      // Act
      val result = disposal_of_vehicle.SetUpTradeDetails.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some("/disposal-of-vehicle/business-choose-your-address"))
    }
  }
}