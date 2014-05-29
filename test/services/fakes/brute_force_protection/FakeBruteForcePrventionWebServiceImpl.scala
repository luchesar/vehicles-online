package services.fakes.brute_force_protection

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import FakeBruteForcePrventionWebServiceImpl._
import play.api.http.Status._
import play.api.libs.ws.Response
import services.brute_force_prevention.BruteForcePreventionWebService
import services.fakes.FakeResponse

final class FakeBruteForcePrventionWebServiceImpl() extends BruteForcePreventionWebService {
  override def callBruteForce(vrm: String): Future[Response] = Future {
    if (vrm == VrmLocked) new FakeResponse(status = FORBIDDEN)
    else new FakeResponse(status = OK)
  }
}

object FakeBruteForcePrventionWebServiceImpl {
  private final val VrmLocked = "st05999"
}