package controllers.disposal_of_vehicle

import mappings.common.Postcode._
import helpers.UnitSpec
import helpers.disposal_of_vehicle._
import mappings.common.AddressAndPostcode._
import mappings.common.AddressLines._
import pages.disposal_of_vehicle._
import play.api.test.Helpers._
import services.fakes.FakeAddressLookupService._
import mappings.disposal_of_vehicle.TraderDetails.TraderDetailsCacheKey
import composition.TestComposition.{testInjector => injector}
import common.CookieHelper._
import helpers.WithApplication

final class EnterAddressManuallyUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val request = FakeCSRFRequest().withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to SetupTraderDetails page when present with no dealer name cached" in new WithApplication {
      val request = FakeCSRFRequest()
      val result = enterAddressManually.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "display populated fields when cookie exists" in new WithApplication {
      val request = FakeCSRFRequest().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.enterAddressManually())
      val result = enterAddressManually.present(request)
      val content = contentAsString(result)
      content should include(line1Valid)
      content should include(line2Valid)
      content should include(line3Valid)
      content should include(line4Valid)
      content should include(postcodeValid)
    }

    "display empty fields when cookie does not exist" in new WithApplication {
      val request = FakeCSRFRequest().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.present(request)
      val content = contentAsString(result)
      content should not include line1Valid
      content should not include line2Valid
      content should not include line3Valid
      content should not include line4Valid
      content should not include postcodeValid
    }
  }

  "submit" should {
    "return bad request when no data is entered" in new WithApplication {
      val request = FakeCSRFRequest().withFormUrlEncodedBody().withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result =  enterAddressManually.submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "return bad request when a valid address is entered without a postcode" in new WithApplication {
      val request = FakeCSRFRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$AddressLinesId.$Line1Id" -> line1Valid,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line2Id" -> line2Valid,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line3Id" -> line3Valid,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line4Id" -> line4Valid).withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "return bad request a valid postcode is entered without an address" in new WithApplication {
      val request = FakeCSRFRequest().withFormUrlEncodedBody(
          s"$AddressAndPostcodeId.$PostcodeId" -> postcodeValid).withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "redirect to Dispose after a valid submission of all fields" in new WithApplication {
      val request = FakeCSRFRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$AddressLinesId.$Line1Id" -> line1Valid,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line2Id" -> line2Valid,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line3Id" -> line3Valid,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line4Id" -> line4Valid,
        s"$AddressAndPostcodeId.$PostcodeId" -> postcodeValid).withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to Dispose after a valid submission of mandatory fields only" in new WithApplication {
      val request = FakeCSRFRequest().withFormUrlEncodedBody(
          s"$AddressAndPostcodeId.$AddressLinesId.$Line1Id" -> line1Valid,
          s"$AddressAndPostcodeId.$AddressLinesId.$Line4Id" -> line4Valid,
          s"$AddressAndPostcodeId.$PostcodeId" -> postcodeValid).withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "submit removes commas and full stops from the end of each address line" in new WithApplication {
      val request = FakeCSRFRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$AddressLinesId.$Line1Id" -> "my house,",
        s"$AddressAndPostcodeId.$AddressLinesId.$Line2Id" -> "my street.",
        s"$AddressAndPostcodeId.$AddressLinesId.$Line3Id" -> "my area.",
        s"$AddressAndPostcodeId.$AddressLinesId.$Line4Id" -> "my town,",
        s"$AddressAndPostcodeId.$PostcodeId" -> postcodeValid).withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r =>
          val cookies = fetchCookiesFromHeaders(r)
          cookies.map(_.name) should contain (TraderDetailsCacheKey)
      }
    }

    "submit removes multiple commas and full stops from the end of each address line" in new WithApplication {
      val request = FakeCSRFRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$AddressLinesId.$Line1Id" -> "my house,.,..,,",
        s"$AddressAndPostcodeId.$AddressLinesId.$Line2Id" -> "my street...,,.,",
        s"$AddressAndPostcodeId.$AddressLinesId.$Line3Id" -> "my area.,,..",
        s"$AddressAndPostcodeId.$AddressLinesId.$Line4Id" -> "my town,,,.,,,.",
        s"$AddressAndPostcodeId.$PostcodeId" -> postcodeValid).withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r =>
          val cookies = fetchCookiesFromHeaders(r)
          cookies.map(_.name) should contain (TraderDetailsCacheKey)
      }
    }

    "submit does not remove multiple commas and full stops from the middle address line" in new WithApplication {
      val request = FakeCSRFRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$AddressLinesId.$Line1Id" -> "my house 1.1,",
        s"$AddressAndPostcodeId.$AddressLinesId.$Line2Id" -> "my street.",
        s"$AddressAndPostcodeId.$AddressLinesId.$Line3Id" -> "my area.",
        s"$AddressAndPostcodeId.$AddressLinesId.$Line4Id" -> "my town,",
        s"$AddressAndPostcodeId.$PostcodeId" -> postcodeValid).withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r =>
          val cookies = fetchCookiesFromHeaders(r)
          cookies.find(_.name == TraderDetailsCacheKey) match {
            case Some(cookie) => cookie.value should include ("my house 1.1")
            case _ => fail("should have found some cookie")
          }
      }
    }

    "submit does not accept an address containing only full stops" in new WithApplication {
      val request = FakeCSRFRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$AddressLinesId.$Line1Id" -> "...",
        s"$AddressAndPostcodeId.$PostcodeId" -> postcodeValid).withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "redirect to SetupTraderDetails page when valid submit with no dealer name cached" in new WithApplication {
      val request = FakeCSRFRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$AddressLinesId.$Line1Id" -> line1Valid,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line2Id" -> line2Valid,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line3Id" -> line3Valid,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line4Id" -> line4Valid,
        s"$AddressAndPostcodeId.$PostcodeId" -> postcodeValid)
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetupTradeDetails page when bad submit with no dealer name cached" in new WithApplication {
      val request = FakeCSRFRequest().withFormUrlEncodedBody()
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "write cookie after a valid submission of all fields" in new WithApplication {
      val request = FakeCSRFRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$AddressLinesId.$Line1Id" -> line1Valid,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line2Id" -> line2Valid,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line3Id" -> line3Valid,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line4Id" -> line4Valid,
        s"$AddressAndPostcodeId.$PostcodeId" -> postcodeValid).withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r =>
          val cookies = fetchCookiesFromHeaders(r)
          cookies.map(_.name) should contain (TraderDetailsCacheKey)
      }
    }

    "collapse error messages for line1" in new WithApplication {
      val request = FakeCSRFRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$AddressLinesId.$Line1Id" -> "",
        s"$AddressAndPostcodeId.$AddressLinesId.$Line4Id" -> line4Valid,
        s"$AddressAndPostcodeId.$PostcodeId" -> postcodeValid).withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r =>
          val content = contentAsString(result)
          content should include("Line 1 requires a minimum length of 4 characters")
      }
    }
  }

  private val enterAddressManually = {
    injector.getInstance(classOf[EnterAddressManually])
  }

}
