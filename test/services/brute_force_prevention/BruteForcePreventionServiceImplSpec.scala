package services.brute_force_prevention

import helpers.UnitSpec
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import services.fakes.{FakeVehicleLookupWebService, FakeResponse}
import org.mockito.Mockito._
import FakeVehicleLookupWebService.registrationNumberValid
import services.fakes.brute_force_protection.FakeBruteForcePreventionWebServiceImpl
import services.fakes.brute_force_protection.FakeBruteForcePreventionWebServiceImpl._
import play.api.libs.ws.Response
import scala.Some
import models.domain.disposal_of_vehicle.BruteForcePreventionViewModel
import utils.helpers.Config

final class BruteForcePreventionServiceImplSpec extends UnitSpec {
  "isVrmLookupPermitted" should {
    "return true when response status is 200 OK" in {
      val service = bruteForceServiceImpl(permitted = true)
      whenReady(service.isVrmLookupPermitted(registrationNumberValid)) {
        r => r should equal(Some(BruteForcePreventionViewModel(true, 1, 3)))
      }
    }

    "return false when response status is not 200 OK" in {
      val service = bruteForceServiceImpl(permitted = false)
      whenReady(service.isVrmLookupPermitted(registrationNumberValid)) {
        r => r should equal(Some(BruteForcePreventionViewModel(false, 1, 1)))
      }
    }

    "return None when webservice call throws" in {
      val service = bruteForceServiceImpl(permitted = true)
      whenReady(service.isVrmLookupPermitted(VrmThrows)) {
        r => r should equal(None)
      }
    }

  }

  private def responseThrows: Future[Response] = Future {
    throw new RuntimeException("This error is generated deliberately by a test")
  }

  private def bruteForceServiceImpl(permitted: Boolean): BruteForcePreventionService = {
    def bruteForcePreventionWebService: BruteForcePreventionWebService = {
      val status = if (permitted) play.api.http.Status.OK else play.api.http.Status.FORBIDDEN
      val bruteForcePreventionWebService: BruteForcePreventionWebService = mock[BruteForcePreventionWebService]

      when(bruteForcePreventionWebService.callBruteForce(registrationNumberValid)).thenReturn(Future {
        new FakeResponse (status = status, fakeJson = attempt1Json)
      })
      when(bruteForcePreventionWebService.callBruteForce(FakeBruteForcePreventionWebServiceImpl.VrmAttempt2)).thenReturn(Future {
        new FakeResponse (status = status, fakeJson = attempt2Json)
      })
      when(bruteForcePreventionWebService.callBruteForce(FakeBruteForcePreventionWebServiceImpl.VrmLocked)).thenReturn(Future {
        new FakeResponse (status = status)
      })
      when(bruteForcePreventionWebService.callBruteForce(VrmThrows)).thenReturn(responseThrows)

      bruteForcePreventionWebService
    }

    new BruteForcePreventionServiceImpl(
      new Config,
      ws = bruteForcePreventionWebService
    )
  }
}
