package controllers.disposal_of_vehicle

import common.CookieHelper._
import composition.TestComposition.{testInjector => injector}
import helpers.UnitSpec
import helpers.WithApplication
import helpers.disposal_of_vehicle._
import mappings.common.AddressAndPostcode._
import mappings.common.AddressLines._
import mappings.common.Postcode._
import mappings.disposal_of_vehicle.TraderDetails.TraderDetailsCacheKey
import pages.disposal_of_vehicle._
import play.api.test.Helpers._
import services.fakes.FakeAddressLookupService._
import play.api.test.FakeRequest

final class EnterAddressManuallyUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to SetupTraderDetails page when present with no dealer name cached" in new WithApplication {
      val request = FakeRequest()
      val result = enterAddressManually.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "display populated fields when cookie exists" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.enterAddressManually())
      val result = enterAddressManually.present(request)
      val content = contentAsString(result)
      content should include(BuildingNameOrNumberValid)
      content should include(Line2Valid)
      content should include(Line3Valid)
      content should include(PostTownValid)
      content should include(PostcodeValid)
    }

    "display empty fields when cookie does not exist" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.present(request)
      val content = contentAsString(result)
      content should not include BuildingNameOrNumberValid
      content should not include Line2Valid
      content should not include Line3Valid
      content should not include PostTownValid
      content should not include PostcodeValid
    }
  }

  "submit" should {
    "return bad request when no data is entered" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())

      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "return bad request when a valid address is entered without a postcode" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$AddressLinesId.$BuildingNameOrNumberId" -> BuildingNameOrNumberValid,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line2Id" -> Line2Valid,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line3Id" -> Line3Valid,
        s"$AddressAndPostcodeId.$AddressLinesId.$postTownId" -> PostTownValid).
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "return bad request a valid postcode is entered without an address" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$PostcodeId" -> PostcodeValid).
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "redirect to Dispose after a valid submission of all fields" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$AddressLinesId.$BuildingNameOrNumberId" -> BuildingNameOrNumberValid,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line2Id" -> Line2Valid,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line3Id" -> Line3Valid,
        s"$AddressAndPostcodeId.$AddressLinesId.$postTownId" -> PostTownValid,
        s"$AddressAndPostcodeId.$PostcodeId" -> PostcodeValid).
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to Dispose after a valid submission of mandatory fields only" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$AddressLinesId.$BuildingNameOrNumberId" -> BuildingNameOrNumberValid,
        s"$AddressAndPostcodeId.$AddressLinesId.$postTownId" -> PostTownValid,
        s"$AddressAndPostcodeId.$PostcodeId" -> PostcodeValid).
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "submit removes commas and full stops from the end of each address line" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$AddressLinesId.$BuildingNameOrNumberId" -> "my house,",
        s"$AddressAndPostcodeId.$AddressLinesId.$Line2Id" -> "my street.",
        s"$AddressAndPostcodeId.$AddressLinesId.$Line3Id" -> "my area.",
        s"$AddressAndPostcodeId.$AddressLinesId.$postTownId" -> "my town,",
        s"$AddressAndPostcodeId.$PostcodeId" -> PostcodeValid).
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r =>
          val cookies = fetchCookiesFromHeaders(r)
          cookies.map(_.name) should contain(TraderDetailsCacheKey)
      }
    }

    "submit removes multiple commas and full stops from the end of each address line" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$AddressLinesId.$BuildingNameOrNumberId" -> "my house,.,..,,",
        s"$AddressAndPostcodeId.$AddressLinesId.$Line2Id" -> "my street...,,.,",
        s"$AddressAndPostcodeId.$AddressLinesId.$Line3Id" -> "my area.,,..",
        s"$AddressAndPostcodeId.$AddressLinesId.$postTownId" -> "my town,,,.,,,.",
        s"$AddressAndPostcodeId.$PostcodeId" -> PostcodeValid).
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r =>
          val cookies = fetchCookiesFromHeaders(r)
          cookies.map(_.name) should contain(TraderDetailsCacheKey)
      }
    }

    "submit does not remove multiple commas and full stops from the middle address line" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$AddressLinesId.$BuildingNameOrNumberId" -> "my house 1.1,",
        s"$AddressAndPostcodeId.$AddressLinesId.$Line2Id" -> "my street.",
        s"$AddressAndPostcodeId.$AddressLinesId.$Line3Id" -> "my area.",
        s"$AddressAndPostcodeId.$AddressLinesId.$postTownId" -> "my town,",
        s"$AddressAndPostcodeId.$PostcodeId" -> PostcodeValid).
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r =>
          val cookies = fetchCookiesFromHeaders(r)
          cookies.find(_.name == TraderDetailsCacheKey) match {
            case Some(cookie) => cookie.value should include("MY HOUSE 1.1")
            case _ => fail("should have found some cookie")
          }
      }
    }

    "submit does not accept an address containing only full stops" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$AddressLinesId.$BuildingNameOrNumberId" -> "...",
        s"$AddressAndPostcodeId.$PostcodeId" -> PostcodeValid).
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "redirect to SetupTraderDetails page when valid submit with no dealer name cached" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$AddressLinesId.$BuildingNameOrNumberId" -> BuildingNameOrNumberValid,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line2Id" -> Line2Valid,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line3Id" -> Line3Valid,
        s"$AddressAndPostcodeId.$AddressLinesId.$postTownId" -> PostTownValid,
        s"$AddressAndPostcodeId.$PostcodeId" -> PostcodeValid)
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetupTradeDetails page when bad submit with no dealer name cached" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody()
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "write cookie after a valid submission of all fields" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$AddressLinesId.$BuildingNameOrNumberId" -> BuildingNameOrNumberValid,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line2Id" -> Line2Valid,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line3Id" -> Line3Valid,
        s"$AddressAndPostcodeId.$AddressLinesId.$postTownId" -> PostTownValid,
        s"$AddressAndPostcodeId.$PostcodeId" -> PostcodeValid).
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      whenReady(result) {
        r =>
          val cookies = fetchCookiesFromHeaders(r)
          cookies.map(_.name) should contain(TraderDetailsCacheKey)
      }
    }

    "collapse error messages for buildingNameOrNumber" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$AddressLinesId.$BuildingNameOrNumberId" -> "",
        s"$AddressAndPostcodeId.$AddressLinesId.$postTownId" -> PostTownValid,
        s"$AddressAndPostcodeId.$PostcodeId" -> PostcodeValid).
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      val content = contentAsString(result)
      content should include("Building name or number - Must contain a minimum of four characters")
    }

    "collapse error messages for post town" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$AddressLinesId.$BuildingNameOrNumberId" -> BuildingNameOrNumberValid,
        s"$AddressAndPostcodeId.$AddressLinesId.$postTownId" -> "",
        s"$AddressAndPostcodeId.$PostcodeId" -> PostcodeValid).
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      val content = contentAsString(result)
      content should include("Post town - Requires a minimum length of three characters")
    }

    "collapse error messages for post code" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody(
        s"$AddressAndPostcodeId.$AddressLinesId.$BuildingNameOrNumberId" -> BuildingNameOrNumberValid,
        s"$AddressAndPostcodeId.$AddressLinesId.$postTownId" -> PostTownValid,
        s"$AddressAndPostcodeId.$PostcodeId" -> "").
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually.submit(request)
      val content = contentAsString(result)
      content should include("Must be between five and eight characters and in a valid format, eg. PR2 8AE or PR28AE")
    }
  }

  private val enterAddressManually = {
    injector.getInstance(classOf[EnterAddressManually])
  }

}
