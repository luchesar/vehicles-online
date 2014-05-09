package controllers.disposal_of_vehicle

import helpers.UnitSpec
import helpers.disposal_of_vehicle.CookieFactoryForUnitSpecs
import mappings.disposal_of_vehicle.BusinessChooseYourAddress._
import pages.disposal_of_vehicle._
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}
import services.fakes.FakeWebServiceImpl
import services.fakes.FakeWebServiceImpl._
import play.api.mvc.Cookies
import mappings.disposal_of_vehicle.TraderDetails._
import scala.Some
import scala.Some

class BusinessChooseYourAddressUnitSpec extends UnitSpec {
  "present" should {
    "present if dealer details cached" in new WithApplication {
      val request = FakeRequest().withSession().withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = businessChooseYourAddressWithUprnFound().present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to setupTradeDetails page when present with no dealer name cached" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest()
      val result = businessChooseYourAddressWithUprnFound().present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }
  }

  "submit" should {
    "redirect to VehicleLookup page after a valid submit" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest().withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = businessChooseYourAddressWithUprnFound().submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "return a bad request if not address selected" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest(traderUprn = "").withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = businessChooseYourAddressWithUprnFound().submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "redirect to setupTradeDetails page when valid submit with no dealer name cached" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest()
      val result = businessChooseYourAddressWithUprnFound().submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to setupTradeDetails page when bad submit with no dealer name cached" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest(traderUprn = "")
      val result = businessChooseYourAddressWithUprnFound().submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to UprnNotFound page when submit with but uprn not found by the webservice" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest().withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = businessChooseYourAddressWithUprnNotFound.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(UprnNotFoundPage.address))
      }
    }

    "write cookie when uprn found" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest().withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = businessChooseYourAddressWithUprnFound().submit(request)
      whenReady(result) {
        r =>
          val cookies = r.header.headers.get(SET_COOKIE).toSeq.flatMap(Cookies.decode)
          val foundBusinessChooseYourAddress = cookies.exists(cookie => cookie.equals(CookieFactoryForUnitSpecs.businessChooseYourAddress()))
          foundBusinessChooseYourAddress should equal(true)

          cookies.map(_.name) should contain allOf (businessChooseYourAddressCacheKey, traderDetailsCacheKey)
      }
    }

    "does not write cookie when uprn not found" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest().withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = businessChooseYourAddressWithUprnNotFound.submit(request)
      whenReady(result) {
        r =>
          val cookies = r.header.headers.get(SET_COOKIE).toSeq.flatMap(Cookies.decode)
          cookies.map(_.name) should contain noneOf (businessChooseYourAddressCacheKey, traderDetailsCacheKey)
      }
    }
  }

  private def businessChooseYourAddressWithFakeWebService(uprnFound: Boolean = true) = {
    val responsePostcode = if (uprnFound) responseValidForPostcodeToAddress else responseValidForPostcodeToAddressNotFound
    val responseUprn = if (uprnFound) responseValidForUprnToAddress else responseValidForUprnToAddressNotFound
    val fakeWebService = new FakeWebServiceImpl(responsePostcode, responseUprn)
    val addressLookupService = new services.address_lookup.ordnance_survey.AddressLookupServiceImpl(fakeWebService)
    new BusinessChooseYourAddress(addressLookupService)
  }

  private def buildCorrectlyPopulatedRequest(traderUprn: String = traderUprnValid.toString) = {
    FakeRequest().withSession().withFormUrlEncodedBody(
      addressSelectId -> traderUprn)
  }

  private def businessChooseYourAddressWithUprnFound() =
    businessChooseYourAddressWithFakeWebService()

  private val businessChooseYourAddressWithUprnNotFound = businessChooseYourAddressWithFakeWebService(uprnFound = false)

}
