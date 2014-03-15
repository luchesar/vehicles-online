package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import org.scalatest.{Matchers, WordSpec}
import mappings.common.AddressLines._
import org.specs2.mock.Mockito
import helpers.disposal_of_vehicle._
import helpers.disposal_of_vehicle.EnterAddressManuallyPage._
import mappings.common.{Postcode, AddressAndPostcode, AddressLines}
import Postcode._
import helpers.disposal_of_vehicle.PostcodePage._
import scala.Some

class EnterAddressManuallyControllerSpec extends WordSpec with Matchers with Mockito {
  "EnterAddressManually - Controller" should {

    "present" in new WithApplication {
      SetUpTradeDetailsPage.setupCache()
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.EnterAddressManually.present(request)

      status(result) should equal(OK)
    }

    "return bad request when no data is entered" in new WithApplication {
      SetUpTradeDetailsPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      val result =  disposal_of_vehicle.EnterAddressManually.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return bad request when a valid address is entered without a postcode" in new WithApplication {
      SetUpTradeDetailsPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line1Id" -> line1Valid,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line2Id" -> line2Valid,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line3Id" -> line3Valid,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line4Id" -> line4Valid)

      val result = disposal_of_vehicle.EnterAddressManually.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return bad request a valid postcode is entered without an address" in new WithApplication {
      SetUpTradeDetailsPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          s"${AddressAndPostcode.id}.$postcodeId" -> postcodeValid)

      val result = disposal_of_vehicle.EnterAddressManually.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "redirect to SetupTraderDetails page when present with no dealer name cached" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.EnterAddressManually.present(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to Dispose after a valid submission of all fields" in new WithApplication {
      SetUpTradeDetailsPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line1Id" -> line1Valid,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line2Id" -> line2Valid,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line3Id" -> line3Valid,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line4Id" -> line4Valid,
          s"${AddressAndPostcode.id}.$postcodeId" -> postcodeValid)

      val result = disposal_of_vehicle.EnterAddressManually.submit(request)

      redirectLocation(result) should equal(Some(VehicleLookupPage.url))
    }

    "redirect to Dispose after a valid submission of mandatory fields only" in new WithApplication {
      SetUpTradeDetailsPage.setupCache()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line1Id" -> line1Valid,
          s"${AddressAndPostcode.id}.$postcodeId" -> postcodeValid)

      val result = disposal_of_vehicle.EnterAddressManually.submit(request)

      redirectLocation(result) should equal(Some(VehicleLookupPage.url))
    }

    "redirect to SetupTraderDetails page when valid submit with no dealer name cached" in new WithApplication {
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line1Id" -> line1Valid,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line2Id" -> line2Valid,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line3Id" -> line3Valid,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line4Id" -> line4Valid,
          s"${AddressAndPostcode.id}.$postcodeId" -> postcodeValid)

      val result = disposal_of_vehicle.EnterAddressManually.submit(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to SetupTradeDetails page when bad submit with no dealer name cached" in new WithApplication {
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody() //Empty form submission

      val result = disposal_of_vehicle.EnterAddressManually.submit(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }
  }
}