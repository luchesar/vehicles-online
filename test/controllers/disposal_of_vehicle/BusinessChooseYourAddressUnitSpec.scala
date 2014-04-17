package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import mappings.disposal_of_vehicle.BusinessChooseYourAddress._
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup
import services.fakes.FakeWebServiceImpl
import helpers.UnitSpec
import services.fakes.FakeWebServiceImpl._

class BusinessChooseYourAddressUnitSpec extends UnitSpec {
  private def businessChooseYourAddressWithFakeWebService(uprnFound: Boolean = true) = {
    val responsePostcode = if(uprnFound) responseValidForPostcodeToAddress else responseValidForPostcodeToAddressNotFound
    val responseUprn = if(uprnFound) responseValidForUprnToAddress else responseValidForUprnToAddressNotFound
    val fakeWebService = new FakeWebServiceImpl(responsePostcode, responseUprn)
    val addressLookupService = new services.address_lookup.ordnance_survey.AddressLookupServiceImpl(fakeWebService)
    new BusinessChooseYourAddress(addressLookupService)
  }

  private def buildCorrectlyPopulatedRequest(traderUprn: String = traderUprnValid.toString) = {
    FakeRequest().withSession().withFormUrlEncodedBody(
      addressSelectId -> traderUprn)
  }

  private def cacheSetup() = {
    CacheSetup.setupTradeDetails()
  }

  private val businessChooseYourAddressWithUprnFound = businessChooseYourAddressWithFakeWebService()

  "BusinessChooseYourAddress - Controller" should {
    "present" in new WithApplication {
      cacheSetup()
      val request = FakeRequest().withSession()
      val result = businessChooseYourAddressWithUprnFound.present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to VehicleLookup page after a valid submit" in new WithApplication {
      cacheSetup()
      val request = buildCorrectlyPopulatedRequest()
      val result = businessChooseYourAddressWithUprnFound.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "return a bad request if not address selected" in new WithApplication {
      cacheSetup()
      val request = buildCorrectlyPopulatedRequest(traderUprn = "")
      val result = businessChooseYourAddressWithUprnFound.submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "redirect to setupTradeDetails page when present with no dealer name cached" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest()
      val result = businessChooseYourAddressWithUprnFound.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to setupTradeDetails page when valid submit with no dealer name cached" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest()
      val result = businessChooseYourAddressWithUprnFound.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to setupTradeDetails page when bad submit with no dealer name cached" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest(traderUprn = "")
      val result = businessChooseYourAddressWithUprnFound.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to UprnNotFound page when submit with but uprn not found by the webservice" in new WithApplication {
      cacheSetup()
      val businessChooseYourAddressWithUprnNotFound = businessChooseYourAddressWithFakeWebService(uprnFound = false)
      val request = buildCorrectlyPopulatedRequest()
      val result = businessChooseYourAddressWithUprnNotFound.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(UprnNotFoundPage.address))
      }
    }
  }
}
