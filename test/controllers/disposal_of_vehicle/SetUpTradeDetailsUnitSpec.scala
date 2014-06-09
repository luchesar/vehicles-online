package controllers.disposal_of_vehicle

import helpers.UnitSpec
import mappings.disposal_of_vehicle.SetupTradeDetails._
import pages.disposal_of_vehicle._
import play.api.test.Helpers._
import services.fakes.FakeAddressLookupService._
import helpers.disposal_of_vehicle.CookieFactoryForUnitSpecs
import composition.TestComposition.{testInjector => injector}
import common.CookieHelper._
import helpers.WithApplication
import play.api.test.FakeRequest
import play.api.Play
import models.domain.disposal_of_vehicle.SetupTradeDetailsModel
import scala.Some
import helpers.JsonUtils.deserializeJsonToModel

final class SetUpTradeDetailsUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val request = FakeRequest()
      val result = setUpTradeDetails.present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "display populated fields when cookie exists" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = setUpTradeDetails.present(request)
      val content = contentAsString(result)
      content should include(TraderBusinessNameValid)
      content should include(PostcodeValid)
    }

    "display empty fields when cookie does not exist" in new WithApplication {
      val request = FakeRequest()
      val result = setUpTradeDetails.present(request)
      val content = contentAsString(result)
      content should not include TraderBusinessNameValid
      content should not include PostcodeValid
    }
  }

  "submit" should {
    "redirect to next page when the form is completed successfully" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest()
      val result = setUpTradeDetails.submit(request)
      whenReady(result) {
        r =>
          r.header.headers.get(LOCATION) should equal(Some(BusinessChooseYourAddressPage.address))
          val cookies = fetchCookiesFromHeaders(r)
          val cookieName = "setupTraderDetails"
          cookies.find(_.name == cookieName) match {
            case Some(cookie) =>
              val json = cookie.value
              val model = deserializeJsonToModel[SetupTradeDetailsModel](json)
              model.traderBusinessName should equal(TraderBusinessNameValid.toUpperCase)
              model.traderPostcode should equal(PostcodeValid.toUpperCase)
            case None => fail(s"$cookieName cookie not found")
          }
      }
    }

    "return a bad request if no details are entered" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest(dealerName = "", dealerPostcode = "")
      val result = setUpTradeDetails.submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "replace max length error message for traderBusinessName with standard error message (US158)" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest(dealerName = "a" * (TraderNameMaxLength + 1))
      val result = setUpTradeDetails.submit(request)
      val count = "Must be between two and 58 characters and not contain invalid characters".r.findAllIn(contentAsString(result)).length
      count should equal(2)
    }

    "replace required and min length error messages for traderBusinessName with standard error message (US158)" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest(dealerName = "")
      val result = setUpTradeDetails.submit(request)
      val count = "Must be between two and 58 characters and not contain invalid characters".r.findAllIn(contentAsString(result)).length
      count should equal(2) // The same message is displayed in 2 places - once in the validation-summary at the top of
      // the page and once above the field.
    }

    "write cookie when the form is completed successfully" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest()
      val result = setUpTradeDetails.submit(request)
      whenReady(result) {
        r =>
          val cookies = fetchCookiesFromHeaders(r)
          cookies.map(_.name) should contain (SetupTradeDetailsCacheKey)
      }
    }
  }

  "withLanguageEn" should {
    "redirect back to the same page" in new WithApplication {
      val result = setUpTradeDetails.withLanguageEn(FakeRequest())
      whenReady(result) {
        r =>
          r.header.status should equal(SEE_OTHER) // Redirect...
          r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address)) // ... back to the same page.
      }
    }

    "writes language cookie set to 'en'" in new WithApplication {
      val result = setUpTradeDetails.withLanguageEn(FakeRequest())
      whenReady(result) {
        r =>
          val cookies = fetchCookiesFromHeaders(r)
          cookies.find(_.name == Play.langCookieName) match {
            case Some(cookie) => cookie.value should equal("en")
            case None => fail("langCookieName not found")
          }
      }
    }
  }

  private def buildCorrectlyPopulatedRequest(dealerName: String = TraderBusinessNameValid, dealerPostcode: String = PostcodeValid) = {
    FakeRequest().withFormUrlEncodedBody(
      TraderNameId -> dealerName,
      TraderPostcodeId -> dealerPostcode)
  }

  private val setUpTradeDetails = {
    injector.getInstance(classOf[SetUpTradeDetails])
  }
}
