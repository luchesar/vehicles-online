package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import org.scalatest.{Matchers, WordSpec}
import mappings.disposal_of_vehicle.Dispose._
import helpers.disposal_of_vehicle.{DisposeSuccessPage, BusinessChooseYourAddressPage, SetUpTradeDetailsPage, VehicleLookupPage}
import helpers.disposal_of_vehicle.Helper._
import org.scalatest.mock.MockitoSugar
import models.domain.disposal_of_vehicle.DisposeModel
import org.mockito.Mockito._
import org.mockito.Matchers._
import scala.Some
import services.fakes.FakeDisposeSuccessService

class DisposeControllerSpec extends WordSpec with Matchers with MockitoSugar {
  "Disposal - Controller" should {
    val mockWebService = mock[services.DisposeService]
    val mockDisposeSuccessModel = mock[DisposeModel]
    when(mockWebService.invoke(any[DisposeModel])).thenReturn(new FakeDisposeSuccessService().invoke(mockDisposeSuccessModel))
    val disposeSuccess = new disposal_of_vehicle.Dispose(mockWebService)

    "present" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.setupCache
      VehicleLookupPage.setupCache
      val request = FakeRequest().withSession()

      // Act
      val result = disposeSuccess.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to dispose success when correct details are entered" in new WithApplication {
      //Arrange
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.setupCache
      VehicleLookupPage.setupCache
      VehicleLookupPage.setupVehicleLookupFormModelCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          consentId -> consentValid,
          mileageId -> mileageValid,
          s"${dateOfDisposalId}.day" -> dateOfDisposalDayValid,
          s"${dateOfDisposalId}.month" -> dateOfDisposalMonthValid,
          s"${dateOfDisposalId}.year" -> dateOfDisposalYearValid
        )

      // Act
      val result = disposeSuccess.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some(DisposeSuccessPage.url))
    }

    "redirect to dispose error when a fail message is return by the fake microservice" in new WithApplication {

    }

    "redirect to setupTradeDetails after the dispose button is clicked and no vehiclelookupformmodel is cached" in new WithApplication {
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
      val result = disposeSuccess.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some(SetUpTradeDetailsPage.url))
    }

    "redirect to setupTradeDetails page when previous pages have not been visited" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = disposeSuccess.present(request)

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
      val result = disposeSuccess.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }
  }
}