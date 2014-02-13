package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import org.scalatest.{Matchers, WordSpec}
import mappings.disposal_of_vehicle.Dispose._
import org.specs2.mock.Mockito
import helpers.disposal_of_vehicle.{BusinessChooseYourAddressPage, SetUpTradeDetailsPage, DisposeConfirmationPage}
import helpers.disposal_of_vehicle.Helper._

class DisposeControllerSpec extends WordSpec with Matchers with Mockito {
  "Disposal - Controller" should {
    "present" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache
      BusinessChooseYourAddressPage.setupCache
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.Dispose.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to next page after the dispose button is clicked" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          consentId -> consentValid,
          mileageId -> mileageValid,
          s"${dateOfDisposalId}.day" -> dateOfDisposalDayValid,
          s"${dateOfDisposalId}.month" -> dateOfDisposalMonthValid,
          s"${dateOfDisposalId}.year" -> dateOfDisposalYearValid
        )

      // Act
      val result = disposal_of_vehicle.Dispose.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some(DisposeConfirmationPage.url))
    }

    "redirect to setupTradeDetails page when user is not logged in" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.Dispose.present(request)

      // Assert
      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }
  }
}