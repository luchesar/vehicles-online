package services.brute_force_prevention

import helpers.UnitSpec
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import services.fakes.{FakeVehicleLookupWebService, FakeResponse}
import play.api.test.Helpers._
import org.mockito.Mockito._
import org.mockito.Matchers._
import play.api.libs.ws.Response
import FakeVehicleLookupWebService.registrationNumberValid
import play.api.libs.json.Json
import services.fakes.brute_force_protection.FakeBruteForcePreventionWebServiceImpl
import services.fakes.brute_force_protection.FakeBruteForcePreventionWebServiceImpl._
import play.api.libs.ws.Response
import scala.Some

final class BruteForcePreventionServiceImplSpec extends UnitSpec {
  /*
  *     */
  "vrmLookupPermitted" should {
    "return true when response status is 200 OK" in {
      val service = bruteForceServiceImpl(permitted = true)
      whenReady(service.vrmLookupPermitted(registrationNumberValid)) {
        r => r should equal((true, 0, 3))
      }
    }

    "return false when response status is not 200 OK" in {
      val service = bruteForceServiceImpl(permitted = false)
      whenReady(service.vrmLookupPermitted(registrationNumberValid)) {
        r => r should equal((false, 0, 3)) // TODO the 2 ints will change values.
      }
    }

    "return false when webservice call throws" in {
      val service = bruteForceServiceImpl(permitted = true)
      whenReady(service.vrmLookupPermitted(VrmThrows)) {
        r => r should equal((false, 0, 0))
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
      ws = bruteForcePreventionWebService)
  }
}
