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

final class BruteForcePreventionServiceImplSpec extends UnitSpec {
  "vrmLookupPermitted" should {
    "return true when response status is 200 OK" in {
      val resp = response(permitted = true)
      val service = bruteForceServiceImpl(resp)
      whenReady(service.vrmLookupPermitted(registrationNumberValid)) {
        r => r should equal((true, 0, 3)) // TODO the 2 ints will change values.
      }
    }

    "return false when response status is not 200 OK" in {
      val resp = response(permitted = false)
      val service = bruteForceServiceImpl(resp)
      whenReady(service.vrmLookupPermitted(registrationNumberValid)) {
        r => r should equal((false, 0, 3)) // TODO the 2 ints will change values.
      }
    }

    "return false when webservice call throws" in {
      val resp = responseThrows
      val service = bruteForceServiceImpl(resp)
      whenReady(service.vrmLookupPermitted(registrationNumberValid)) {
        r => r should equal((false, 0, 0))
      }
    }
  }

  private def response(permitted: Boolean): Future[Response] = Future {
    val status = if (permitted) OK else FORBIDDEN
    new FakeResponse(status = status)
  }

  private def responseThrows: Future[Response] = Future {
    throw new RuntimeException("This error is generated deliberately by a test")
  }

  private def bruteForceServiceImpl(resp: Future[Response]): BruteForcePreventionService = {
    def bruteForcePreventionWebService(resp: Future[Response]): BruteForcePreventionWebService = {
      val bruteForcePreventionWebService: BruteForcePreventionWebService = mock[BruteForcePreventionWebService]
      when(bruteForcePreventionWebService.callBruteForce(anyString())).thenReturn(resp)
      bruteForcePreventionWebService
    }

    new BruteForcePreventionServiceImpl(
      ws = bruteForcePreventionWebService(resp))
  }
}
