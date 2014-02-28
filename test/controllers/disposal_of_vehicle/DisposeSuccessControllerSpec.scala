package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import helpers.disposal_of_vehicle.{VehicleLookupPage, DisposePage, BusinessChooseYourAddressPage, SetUpTradeDetailsPage}

class DisposeSuccessControllerSpec extends WordSpec with Matchers with Mockito {

  "Disposal confirmation controller" should {

    "present" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.setupCache
      VehicleLookupPage.setupCache
      DisposePage.setupCache
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.DisposeSuccess.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to next page after the new disposal button is clicked" in new WithApplication {
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.setupCache
      VehicleLookupPage.setupCache
      DisposePage.setupCache
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.DisposeSuccess.submit(request)

      // Assert
      redirectLocation(result) should equal(Some(VehicleLookupPage.url)) 
    }

    "redirect to setupTradeDetails page when previous pages have not been visited" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.DisposeSuccess.present(request)

      // Assert
      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

  }
}