package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import mappings.disposal_of_vehicle.BusinessChooseYourAddress._
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup
import services.fakes.FakeWebServiceImpl
import helpers.UnitSpec
import services.fakes.FakeWebServiceImpl._
import services.session.{SessionState, PlaySessionState}

class BusinessChooseYourAddressUnitSpec extends UnitSpec {
  "present" should {
    "present if dealer details cached" in new WithApplication {
      val sessionState = newSessionState
      cacheSetup(sessionState.inner)
      val request = FakeRequest().withSession()
      val result = businessChooseYourAddressWithUprnFound(sessionState).present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to setupTradeDetails page when present with no dealer name cached" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest()
      val result = businessChooseYourAddressWithUprnFound(newSessionState).present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }
  }

  "submit" should {
    "redirect to VehicleLookup page after a valid submit" in new WithApplication {
      val sessionState = newSessionState
      cacheSetup(sessionState.inner)
      val request = buildCorrectlyPopulatedRequest()
      val result = businessChooseYourAddressWithUprnFound(sessionState).submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "return a bad request if not address selected" in new WithApplication {
      val sessionState = newSessionState
      cacheSetup(sessionState.inner)
      val request = buildCorrectlyPopulatedRequest(traderUprn = "")
      val result = businessChooseYourAddressWithUprnFound(sessionState).submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "redirect to setupTradeDetails page when valid submit with no dealer name cached" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest()
      val result = businessChooseYourAddressWithUprnFound(newSessionState).submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to setupTradeDetails page when bad submit with no dealer name cached" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest(traderUprn = "")
      val result = businessChooseYourAddressWithUprnFound(newSessionState).submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to UprnNotFound page when submit with but uprn not found by the webservice" in new WithApplication {
      val sessionState = newSessionState
      cacheSetup(sessionState.inner)
      val businessChooseYourAddressWithUprnNotFound = businessChooseYourAddressWithFakeWebService(sessionState, uprnFound = false)
      val request = buildCorrectlyPopulatedRequest()
      val result = businessChooseYourAddressWithUprnNotFound.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(UprnNotFoundPage.address))
      }
    }
  }

  private def businessChooseYourAddressWithFakeWebService(sessionState: DisposalOfVehicleSessionState, uprnFound: Boolean = true) = {
    val responsePostcode = if (uprnFound) responseValidForPostcodeToAddress else responseValidForPostcodeToAddressNotFound
    val responseUprn = if (uprnFound) responseValidForUprnToAddress else responseValidForUprnToAddressNotFound
    val fakeWebService = new FakeWebServiceImpl(responsePostcode, responseUprn)
    val addressLookupService = new services.address_lookup.ordnance_survey.AddressLookupServiceImpl(fakeWebService)
    new BusinessChooseYourAddress(sessionState, addressLookupService)
  }

  private def buildCorrectlyPopulatedRequest(traderUprn: String = traderUprnValid.toString) = {
    FakeRequest().withSession().withFormUrlEncodedBody(
      addressSelectId -> traderUprn)
  }

  private def cacheSetup(sessionState: SessionState) = {
    new CacheSetup(sessionState).setupTradeDetails()
  }

  private def businessChooseYourAddressWithUprnFound(sessionState: DisposalOfVehicleSessionState) =
    businessChooseYourAddressWithFakeWebService(sessionState)

  private def newSessionState = {
    val sessionState = new PlaySessionState()
    new DisposalOfVehicleSessionState(sessionState)
  }
}
