package controllers.disposal_of_vehicle

import org.scalatest.{Matchers, WordSpec}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import helpers.disposal_of_vehicle.{SetUpTradeDetailsPage, VehicleLookupPage, UprnNotFoundPage}
import mappings.disposal_of_vehicle.BusinessChooseYourAddress._
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers._
import services.fakes.{FakeAddressLookupService, FakeWebServiceImpl}

class BusinessChooseYourAddressControllerSpec extends WordSpec with Matchers with MockitoSugar {

  "BusinessChooseYourAddress - Controller" should {
    val fakeWebService = new FakeWebServiceImpl()
    val fakeAddressLookupService = new FakeAddressLookupService(fakeWebService)
    val businessChooseYourAddress = new BusinessChooseYourAddress(fakeAddressLookupService)

    "present" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache()
      val request = FakeRequest().withSession()

      // Act
      val result = businessChooseYourAddress.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to VehicleLookup page after a valid submit" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(addressSelectId -> "1234")

      // Act
      val result = businessChooseYourAddress.submit(request)

      // Assert
      redirectLocation(result) should equal(Some(VehicleLookupPage.url))
    }

    "return a bad request after no submission" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      // Act
      val result = businessChooseYourAddress.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "return a bad request after a blank submission" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(addressSelectId -> "")

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

    "redirect to setupTradeDetails page when valid submit with no dealer name cached" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(addressSelectId -> "1234")

      // Act
      val result = businessChooseYourAddress.submit(request)

      // Assert
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

      // Act
      val result = businessChooseYourAddress.submit(request)

      // Assert
      redirectLocation(result) should equal(Some(UprnNotFoundPage.url))
    }
  }
}
