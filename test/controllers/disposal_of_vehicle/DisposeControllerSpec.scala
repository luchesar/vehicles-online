package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import org.scalatest.{Matchers, WordSpec}
import mappings.disposal_of_vehicle.Dispose._
import org.specs2.mock.Mockito
import helpers.disposal_of_vehicle.{DisposeSuccessPage, BusinessChooseYourAddressPage, SetUpTradeDetailsPage, VehicleLookupPage}
import helpers.disposal_of_vehicle.Helper._
import org.scalatest.mock.MockitoSugar
import models.domain.disposal_of_vehicle.DisposeModel
import org.mockito.Mockito._
import models.domain.disposal_of_vehicle.VehicleLookupFormModel
import scala.Some
import org.mockito.Matchers._
import models.domain.disposal_of_vehicle.VehicleLookupFormModel
import scala.Some
import services.fakes.FakeDisposeService

class DisposeControllerSpec extends WordSpec with Matchers with MockitoSugar {
  "Disposal - Controller" should {
    val mockDisposeModel = mock[DisposeModel]
    val mockWebService = mock[services.DisposeService]
    when(mockWebService.invoke(any[DisposeModel])).thenReturn(new FakeDisposeService().invoke(mockDisposeModel))
    val dispose = new disposal_of_vehicle.Dispose(mockWebService)

    "present" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.setupCache
      VehicleLookupPage.setupCache
      val request = FakeRequest().withSession()

      // Act
      val result = dispose.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to next page after the dispose button is clicked" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          consentId -> consentValid,
          mileageId -> mileageValid,
          s"${dateOfDisposalId}.day" -> dateOfDisposalDayValid,
          s"${dateOfDisposalId}.month" -> dateOfDisposalMonthValid,
          s"${dateOfDisposalId}.year" -> dateOfDisposalYearValid
        )

      // Act
      val result = dispose.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some(DisposeSuccessPage.url))
    }

    "redirect to setupTradeDetails page when previous pages have not been visited" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = dispose.present(request)

      // Assert
      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "return a bad request when no details are entered" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.setupCache
      VehicleLookupPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      // Act
      val result = dispose.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }
  }
}