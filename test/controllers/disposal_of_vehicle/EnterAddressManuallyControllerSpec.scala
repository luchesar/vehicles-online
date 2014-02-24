package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import org.scalatest.{Matchers, WordSpec}
import mappings.common.AddressLines._
import org.specs2.mock.Mockito
import helpers.disposal_of_vehicle._
import helpers.disposal_of_vehicle.EnterAddressManuallyPage._
import mappings.common.{AddressAndPostcode, AddressLines}
import mappings.disposal_of_vehicle.Postcode._
import helpers.disposal_of_vehicle.PostcodePage._
import scala.Some

class EnterAddressManuallyControllerSpec extends WordSpec with Matchers with Mockito {
  "EnterAddressManually - Controller" should {

    "present" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.EnterAddressManually.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "reject when no data is entered" in new WithApplication {
      SetUpTradeDetailsPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      // Act
      val result =  disposal_of_vehicle.EnterAddressManually.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "reject when a valid address is entered without a postcode" in new WithApplication {
      SetUpTradeDetailsPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line1Id" -> line1Valid,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line2Id" -> line2Valid,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line3Id" -> line3Valid,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line4Id" -> line4Valid)

      // Act
      val result = disposal_of_vehicle.EnterAddressManually.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "reject when a valid postcode is entered without an address" in new WithApplication {
      SetUpTradeDetailsPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          s"${AddressAndPostcode.id}.$postcodeID" -> postcodeValid)

      // Act
      val result = disposal_of_vehicle.EnterAddressManually.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }

    "redirect to setupTradeDetails page when present with no dealer name cached" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.EnterAddressManually.present(request)

      // Assert
      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to next page after a valid submition of all fields" in new WithApplication {
      SetUpTradeDetailsPage.setupCache
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line1Id" -> line1Valid,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line2Id" -> line2Valid,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line3Id" -> line3Valid,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line4Id" -> line4Valid,
          s"${AddressAndPostcode.id}.$postcodeID" -> postcodeValid)

      // Act
      val result = disposal_of_vehicle.EnterAddressManually.submit(request)

      // Assert
      redirectLocation(result) should equal(Some(EnterAddressManuallyPage.url))
      //ToDo Need to look at caching manual address (once new cache built) in order to look at VehicleLookup page
    }

    "redirect to setupTradeDetails page when valid submit with no dealer name cached" in new WithApplication {
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line1Id" -> line1Valid,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line2Id" -> line2Valid,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line3Id" -> line3Valid,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line4Id" -> line4Valid,
          s"${AddressAndPostcode.id}.$postcodeID" -> postcodeValid)

      // Act
      val result = disposal_of_vehicle.EnterAddressManually.submit(request)

      // Assert
      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to setupTradeDetails page when bad submit with no dealer name cached" in new WithApplication {
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody() //Empty form submission

      // Act
      val result = disposal_of_vehicle.EnterAddressManually.submit(request)

      // Assert
      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }
  }
}