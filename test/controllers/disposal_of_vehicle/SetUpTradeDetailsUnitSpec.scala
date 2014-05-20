package controllers.disposal_of_vehicle

import helpers.UnitSpec
import helpers.disposal_of_vehicle.Helper._
import mappings.disposal_of_vehicle.SetupTradeDetails._
import pages.disposal_of_vehicle._
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}
import services.fakes.FakeAddressLookupService._
import play.api.mvc.Cookies
import helpers.disposal_of_vehicle.CookieFactoryForUnitSpecs
import utils.helpers.{CookieNameHashing, NoHash, CookieEncryption, NoEncryption}

class SetUpTradeDetailsUnitSpec extends UnitSpec {
  "present" should {
    "display page" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = setUpTradeDetails().present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "display populated fields when cookie exists" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = setUpTradeDetails().present(request)
      val content = contentAsString(result)
      content should include(traderBusinessNameValid)
      content should include(postcodeValid)
    }

    "display empty fields when cookie does not exist" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = setUpTradeDetails().present(request)
      val content = contentAsString(result)
      content should not include traderBusinessNameValid
      content should not include postcodeValid
    }
  }

  "submit" should {
    "redirect to next page when the form is completed successfully" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest()
      val result = setUpTradeDetails().submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(BusinessChooseYourAddressPage.address))
      }
    }

    "return a bad request if no details are entered" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest(dealerName = "", dealerPostcode = "")
      val result = setUpTradeDetails().submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "replace max length error message for traderBusinessName with standard error message (US158)" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest(dealerName = "a" * (traderNameMaxLength + 1))
      val result = setUpTradeDetails().submit(request)
      val count = countSubstring(contentAsString(result), "Must be between two and 30 characters and not contain invalid characters")
      count should equal(2)
    }

    "replace required and min length error messages for traderBusinessName with standard error message (US158)" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest(dealerName = "")
      val result = setUpTradeDetails().submit(request)
      val count = countSubstring(contentAsString(result), "Must be between two and 30 characters and not contain invalid characters")
      count should equal(2) // The same message is displayed in 2 places - once in the validation-summary at the top of
      // the page and once above the field.
    }

    "write cookie when the form is completed successfully" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest()
      val result = setUpTradeDetails().submit(request)
      whenReady(result) {
        r =>
          val cookies = r.header.headers.get(SET_COOKIE).toSeq.flatMap(Cookies.decode)
          cookies.map(_.name) should contain (SetupTradeDetailsCacheKey)
      }
    }
  }

  private def buildCorrectlyPopulatedRequest(dealerName: String = traderBusinessNameValid, dealerPostcode: String = postcodeValid) = {
    FakeRequest().withSession().withFormUrlEncodedBody(
      traderNameId -> dealerName,
      traderPostcodeId -> dealerPostcode)
  }

  private def setUpTradeDetails() = {
    val noCookieEncryption = new NoEncryption with CookieEncryption
    val noCookieNameHashing = new NoHash with CookieNameHashing
    new SetUpTradeDetails()(noCookieEncryption, noCookieNameHashing)
  }

}
