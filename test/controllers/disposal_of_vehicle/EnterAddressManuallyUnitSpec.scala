package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle._
import mappings.common.Postcode
import Postcode._
import helpers.UnitSpec
import services.fakes.FakeAddressLookupService._
import mappings.common.AddressAndPostcode._
import mappings.common.AddressLines._
import services.session.{SessionState, PlaySessionState}

class EnterAddressManuallyUnitSpec extends UnitSpec {

  "EnterAddressManually - Controller" should {

    "present" in new WithApplication {
      val sessionState = newSessionState
      cacheSetup(sessionState.inner)
      val request = FakeRequest().withSession()
      val result = enterAddressManually(sessionState).present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "return bad request when no data is entered" in new WithApplication {
      val sessionState = newSessionState
      cacheSetup(sessionState.inner)
      val request = FakeRequest().withSession().withFormUrlEncodedBody()
      val result =  enterAddressManually(sessionState).submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "return bad request when a valid address is entered without a postcode" in new WithApplication {
      val sessionState = newSessionState
      cacheSetup(sessionState.inner)
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        s"$addressAndPostcodeId.$addressLinesId.$line1Id" -> line1Valid,
        s"$addressAndPostcodeId.$addressLinesId.$line2Id" -> line2Valid,
        s"$addressAndPostcodeId.$addressLinesId.$line3Id" -> line3Valid,
        s"$addressAndPostcodeId.$addressLinesId.$line4Id" -> line4Valid)
      val result = enterAddressManually(sessionState).submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "return bad request a valid postcode is entered without an address" in new WithApplication {
      val sessionState = newSessionState
      cacheSetup(sessionState.inner)
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
          s"$addressAndPostcodeId.$postcodeId" -> postcodeValid)
      val result = enterAddressManually(sessionState).submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "redirect to SetupTraderDetails page when present with no dealer name cached" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = enterAddressManually(newSessionState).present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to Dispose after a valid submission of all fields" in new WithApplication {
      val sessionState = newSessionState
      cacheSetup(sessionState.inner)
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        s"$addressAndPostcodeId.$addressLinesId.$line1Id" -> line1Valid,
        s"$addressAndPostcodeId.$addressLinesId.$line2Id" -> line2Valid,
        s"$addressAndPostcodeId.$addressLinesId.$line3Id" -> line3Valid,
        s"$addressAndPostcodeId.$addressLinesId.$line4Id" -> line4Valid,
        s"$addressAndPostcodeId.$postcodeId" -> postcodeValid)
      val result = enterAddressManually(sessionState).submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to Dispose after a valid submission of mandatory fields only" in new WithApplication {
      val sessionState = newSessionState
      cacheSetup(sessionState.inner)
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
          s"$addressAndPostcodeId.$addressLinesId.$line1Id" -> line1Valid,
          s"$addressAndPostcodeId.$postcodeId" -> postcodeValid)
      val result = enterAddressManually(sessionState).submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "submit removes commas and full stops from the end of each address line" in new WithApplication {
      val sessionState = newSessionState
      cacheSetup(sessionState.inner)
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        s"$addressAndPostcodeId.$addressLinesId.$line1Id" -> "my house,",
        s"$addressAndPostcodeId.$addressLinesId.$line2Id" -> "my street.",
        s"$addressAndPostcodeId.$addressLinesId.$line3Id" -> "my area.",
        s"$addressAndPostcodeId.$addressLinesId.$line4Id" -> "my town,",
        s"$addressAndPostcodeId.$postcodeId" -> postcodeValid)
      val result = enterAddressManually(sessionState).submit(request)
      whenReady(result) {
        r => sessionState.fetchDealerDetailsFromCache match {
          case Some(f) =>
            f.dealerAddress.address should equal (List("my house", "my street", "my area", "my town", "CM81QJ"))
          case _ => fail("Should have found model in the cache")
        }
      }
    }

    "submit removes multiple commas and full stops from the end of each address line" in new WithApplication {
      val sessionState = newSessionState
      cacheSetup(sessionState.inner)
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        s"$addressAndPostcodeId.$addressLinesId.$line1Id" -> "my house,.,..,,",
        s"$addressAndPostcodeId.$addressLinesId.$line2Id" -> "my street...,,.,",
        s"$addressAndPostcodeId.$addressLinesId.$line3Id" -> "my area.,,..",
        s"$addressAndPostcodeId.$addressLinesId.$line4Id" -> "my town,,,.,,,.",
        s"$addressAndPostcodeId.$postcodeId" -> postcodeValid)
      val result = enterAddressManually(sessionState).submit(request)
      whenReady(result) {
        r => sessionState.fetchDealerDetailsFromCache match {
          case Some(f) =>
            f.dealerAddress.address should equal (List("my house", "my street", "my area", "my town", "CM81QJ"))
          case _ => fail("Should have found model in the cache")
        }
      }
    }

    "submit does not remove multiple commas and full stops from the middle address line" in new WithApplication {
      val sessionState = newSessionState
      cacheSetup(sessionState.inner)
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        s"$addressAndPostcodeId.$addressLinesId.$line1Id" -> "flat 1.1",
        s"$addressAndPostcodeId.$addressLinesId.$line2Id" -> "long road, off high street",
        s"$addressAndPostcodeId.$addressLinesId.$line3Id" -> "little village, my town",
        s"$addressAndPostcodeId.$addressLinesId.$line4Id" -> "my city, my county",
        s"$addressAndPostcodeId.$postcodeId" -> postcodeValid)
      val result = enterAddressManually(sessionState).submit(request)
      whenReady(result) {
        r => sessionState.fetchDealerDetailsFromCache match {
          case Some(f) =>
            f.dealerAddress.address should equal (List("flat 1.1", "long road, off high street", "little village, my town", "my city, my county", "CM81QJ"))
          case _ => fail("Should have found model in the cache")
        }
      }
    }

    "submit does not accept an address containing only full stops" in new WithApplication {
      val sessionState = newSessionState
      cacheSetup(sessionState.inner)
      val request = FakeRequest().withSession().withFormUrlEncodedBody(
        s"$addressAndPostcodeId.$addressLinesId.$line1Id" -> "...",
        s"$addressAndPostcodeId.$postcodeId" -> postcodeValid)
      val result = enterAddressManually(sessionState).submit(request)
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
      val result = enterAddressManually(newSessionState).submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetupTradeDetails page when bad submit with no dealer name cached" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody()
      val result = enterAddressManually(newSessionState).submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }
  }

  private def cacheSetup(sessionState: SessionState) = 
    new CacheSetup(sessionState).setupTradeDetails()

  private def enterAddressManually(sessionState: DisposalOfVehicleSessionState) =
    new EnterAddressManually(sessionState)

  private def newSessionState = {
    val sessionState = new PlaySessionState()
    new DisposalOfVehicleSessionState(sessionState)
  }
}
