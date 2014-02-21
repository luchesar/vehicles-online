package controllers.disposal_of_vehicle

import org.scalatest.{Matchers, WordSpec}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import helpers.disposal_of_vehicle.{SetUpTradeDetailsPage, VehicleLookupPage}
import mappings.disposal_of_vehicle.BusinessAddressSelect._
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers._
import modules.TestModule.FakeAddressLookupService
import helpers.disposal_of_vehicle.BusinessChooseYourAddressPage._

class BusinessChooseYourAddressControllerSpec extends WordSpec with Matchers with MockitoSugar {

  "BusinessChooseYourAddress - Controller" should {
    val mockAddressLookupService = mock[services.AddressLookupService]
    when(mockAddressLookupService.fetchAddress(anyString())).thenReturn(new FakeAddressLookupService().fetchAddress("TEST"))
    val businessChooseYourAddress = new BusinessChooseYourAddress(mockAddressLookupService)

    "present" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache
      val request = FakeRequest().withSession()

      // Act
      val result = businessChooseYourAddress.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to next page after a valid submit" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          addressSelectId -> address1.toViewFormat())

      // Act
      val result = businessChooseYourAddress.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some(VehicleLookupPage.url))
    }

    "return a bad request after an invalid submission" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      // Act
      val result = businessChooseYourAddress.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "redirect to setupTradeDetails page when present with no dealer name cached" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = businessChooseYourAddress.present(request)

      // Assert
      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    // TODO "redirect to setupTradeDetails page when valid submit with no dealer name cached"
    // TODO "redirect to setupTradeDetails page when bad submit with no dealer name cached"
  }
}
