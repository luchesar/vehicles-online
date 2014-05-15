package controllers.disposal_of_vehicle

import mappings.common.Postcode._
import helpers.UnitSpec
import helpers.disposal_of_vehicle._
import mappings.common.AddressAndPostcode._
import mappings.common.AddressLines._
import pages.disposal_of_vehicle._
import play.api.mvc.Cookies
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}
import services.fakes.FakeAddressLookupService._
import mappings.disposal_of_vehicle.TraderDetails.traderDetailsCacheKey
import utils.helpers.{CookieEncryption, NoEncryption}

class EnterAddressManuallyUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val request = FakeRequest().withSession().withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually().present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to SetupTraderDetails page when present with no dealer name cached" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = enterAddressManually().present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "display populated fields when cookie exists" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.enterAddressManually())
      val result = enterAddressManually().present(request)
      val content = contentAsString(result)
      content should include(line1Valid)
      content should include(line2Valid)
      content should include(line3Valid)
      content should include(line4Valid)
      content should include(postcodeValid)
    }

    "display empty fields when cookie does not exist" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually().present(request)
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
      val request = FakeRequest().withSession().withFormUrlEncodedBody().withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result =  enterAddressManually().submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "return bad request when a valid address is entered without a postcode" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        s"$addressAndPostcodeId.$addressLinesId.$line1Id" -> line1Valid,
        s"$addressAndPostcodeId.$addressLinesId.$line2Id" -> line2Valid,
        s"$addressAndPostcodeId.$addressLinesId.$line3Id" -> line3Valid,
        s"$addressAndPostcodeId.$addressLinesId.$line4Id" -> line4Valid).withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually().submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "return bad request a valid postcode is entered without an address" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
          s"$addressAndPostcodeId.$postcodeId" -> postcodeValid).withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually().submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "redirect to Dispose after a valid submission of all fields" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        s"$addressAndPostcodeId.$addressLinesId.$line1Id" -> line1Valid,
        s"$addressAndPostcodeId.$addressLinesId.$line2Id" -> line2Valid,
        s"$addressAndPostcodeId.$addressLinesId.$line3Id" -> line3Valid,
        s"$addressAndPostcodeId.$addressLinesId.$line4Id" -> line4Valid,
        s"$addressAndPostcodeId.$postcodeId" -> postcodeValid).withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually().submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to Dispose after a valid submission of mandatory fields only" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
          s"$addressAndPostcodeId.$addressLinesId.$line1Id" -> line1Valid,
          s"$addressAndPostcodeId.$postcodeId" -> postcodeValid).withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually().submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "submit removes commas and full stops from the end of each address line" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        s"$addressAndPostcodeId.$addressLinesId.$line1Id" -> "my house,",
        s"$addressAndPostcodeId.$addressLinesId.$line2Id" -> "my street.",
        s"$addressAndPostcodeId.$addressLinesId.$line3Id" -> "my area.",
        s"$addressAndPostcodeId.$addressLinesId.$line4Id" -> "my town,",
        s"$addressAndPostcodeId.$postcodeId" -> postcodeValid).withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually().submit(request)
      whenReady(result) {
        r =>
          val cookies = r.header.headers.get(SET_COOKIE).toSeq.flatMap(Cookies.decode)
          val foundMatch = cookies.exists(cookie => cookie.equals(CookieFactoryForUnitSpecs.traderDetailsModel()))
          foundMatch should equal(true)
      }
    }

    "submit removes multiple commas and full stops from the end of each address line" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        s"$addressAndPostcodeId.$addressLinesId.$line1Id" -> "my house,.,..,,",
        s"$addressAndPostcodeId.$addressLinesId.$line2Id" -> "my street...,,.,",
        s"$addressAndPostcodeId.$addressLinesId.$line3Id" -> "my area.,,..",
        s"$addressAndPostcodeId.$addressLinesId.$line4Id" -> "my town,,,.,,,.",
        s"$addressAndPostcodeId.$postcodeId" -> postcodeValid).withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually().submit(request)
      whenReady(result) {
        r =>
          val cookies = r.header.headers.get(SET_COOKIE).toSeq.flatMap(Cookies.decode)
          val foundMatch =  cookies.exists(cookie => cookie.equals(CookieFactoryForUnitSpecs.traderDetailsModel()))
          foundMatch should equal(true)
      }
    }

    "submit does not remove multiple commas and full stops from the middle address line" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        s"$addressAndPostcodeId.$addressLinesId.$line1Id" -> "my house 1.1,",
        s"$addressAndPostcodeId.$addressLinesId.$line2Id" -> "my street.",
        s"$addressAndPostcodeId.$addressLinesId.$line3Id" -> "my area.",
        s"$addressAndPostcodeId.$addressLinesId.$line4Id" -> "my town,",
        s"$addressAndPostcodeId.$postcodeId" -> postcodeValid).withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually().submit(request)
      whenReady(result) {
        r =>
          val cookies = r.header.headers.get(SET_COOKIE).toSeq.flatMap(Cookies.decode)
          val foundMatch =  cookies.exists(cookie => cookie.equals(CookieFactoryForUnitSpecs.traderDetailsModel(line1 = "my house 1.1")))
          foundMatch should equal(true)
      }
    }

    "submit does not accept an address containing only full stops" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        s"$addressAndPostcodeId.$addressLinesId.$line1Id" -> "...",
        s"$addressAndPostcodeId.$postcodeId" -> postcodeValid).withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually().submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "redirect to SetupTraderDetails page when valid submit with no dealer name cached" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        s"$addressAndPostcodeId.$addressLinesId.$line1Id" -> line1Valid,
        s"$addressAndPostcodeId.$addressLinesId.$line2Id" -> line2Valid,
        s"$addressAndPostcodeId.$addressLinesId.$line3Id" -> line3Valid,
        s"$addressAndPostcodeId.$addressLinesId.$line4Id" -> line4Valid,
        s"$addressAndPostcodeId.$postcodeId" -> postcodeValid)
      val result = enterAddressManually().submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetupTradeDetails page when bad submit with no dealer name cached" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody()
      val result = enterAddressManually().submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "write cookie after a valid submission of all fields" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        s"$addressAndPostcodeId.$addressLinesId.$line1Id" -> line1Valid,
        s"$addressAndPostcodeId.$addressLinesId.$line2Id" -> line2Valid,
        s"$addressAndPostcodeId.$addressLinesId.$line3Id" -> line3Valid,
        s"$addressAndPostcodeId.$addressLinesId.$line4Id" -> line4Valid,
        s"$addressAndPostcodeId.$postcodeId" -> postcodeValid).withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = enterAddressManually().submit(request)
      whenReady(result) {
        r =>
          val cookies = r.header.headers.get(SET_COOKIE).toSeq.flatMap(Cookies.decode)
          cookies.map(_.name) should contain (traderDetailsCacheKey)
      }
    }
  }

  private def enterAddressManually() = {
    val noCookieEncryption = new NoEncryption with CookieEncryption
    new EnterAddressManually()(noCookieEncryption)
  }

}
