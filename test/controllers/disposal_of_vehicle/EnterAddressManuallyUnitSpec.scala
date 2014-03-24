package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import mappings.common.AddressLines._
import scala.Some
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.Helper._
import helpers.disposal_of_vehicle._
import mappings.common.{Postcode, AddressAndPostcode, AddressLines}
import Postcode._
import helpers.UnitSpec
import mappings.disposal_of_vehicle.VehicleLookup._
import scala.Some

class EnterAddressManuallyUnitSpec extends UnitSpec {
  "EnterAddressManually - Controller" should {

    "present" in new WithApplication {
      CacheSetup.setupTradeDetails()

      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.EnterAddressManually.present(request)

      status(result) should equal(OK)
    }

    "return bad request when no data is entered" in new WithApplication {
      CacheSetup.setupTradeDetails()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      val result =  disposal_of_vehicle.EnterAddressManually.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "return bad request when a valid address is entered without a postcode" in new WithApplication {
      CacheSetup.setupTradeDetails()
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
      CacheSetup.setupTradeDetails()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          s"${AddressAndPostcode.id}.$postcodeId" -> postcodeValid)

      val result = disposal_of_vehicle.EnterAddressManually.submit(request)

      status(result) should equal(BAD_REQUEST)
    }

    "redirect to SetupTraderDetails page when present with no dealer name cached" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.EnterAddressManually.present(request)

      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.address))
    }

    "redirect to Dispose after a valid submission of all fields" in new WithApplication {
      CacheSetup.setupTradeDetails()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line1Id" -> line1Valid,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line2Id" -> line2Valid,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line3Id" -> line3Valid,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line4Id" -> line4Valid,
          s"${AddressAndPostcode.id}.$postcodeId" -> postcodeValid)

      val result = disposal_of_vehicle.EnterAddressManually.submit(request)

      redirectLocation(result) should equal(Some(VehicleLookupPage.address))
    }

    "redirect to Dispose after a valid submission of mandatory fields only" in new WithApplication {
      CacheSetup.setupTradeDetails()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line1Id" -> line1Valid,
          s"${AddressAndPostcode.id}.$postcodeId" -> postcodeValid)

      val result = disposal_of_vehicle.EnterAddressManually.submit(request)

      redirectLocation(result) should equal(Some(VehicleLookupPage.address))
    }

    "submit removes commas and full stops from address" in new WithApplication {
      CacheSetup.setupTradeDetails()
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line1Id" -> "my,house.",
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line2Id" -> "my,street.",
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line3Id" -> "my,area.",
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line4Id" -> "my,town.",
          s"${AddressAndPostcode.id}.$postcodeId" -> postcodeValid)

      val result = disposal_of_vehicle.EnterAddressManually.submit(request)

      whenReady(result) {
        r => controllers.disposal_of_vehicle.Helpers.fetchDealerDetailsFromCache match {
          case Some(f) => {
            f.dealerAddress.address should equal (List("myhouse", "mystreet", "myarea", "mytown", "CM81QJ"))
          }
          case _ => fail("Should have found model in the cache")
        }
      }
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

      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.address))
    }

    "redirect to SetupTradeDetails page when bad submit with no dealer name cached" in new WithApplication {
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      val result = disposal_of_vehicle.EnterAddressManually.submit(request)

      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.address))
    }
  }
}