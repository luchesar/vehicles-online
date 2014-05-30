package services.fakes.brute_force_protection

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import FakeBruteForcePreventionWebServiceImpl._
import play.api.http.Status._
import play.api.libs.ws.Response
import services.brute_force_prevention.BruteForcePreventionWebService
import services.fakes.FakeResponse

final class FakeBruteForcePreventionWebServiceImpl() extends BruteForcePreventionWebService {
  override def callBruteForce(vrm: String): Future[Response] = Future {
    vrm match {
      case VrmLocked => new FakeResponse(status = FORBIDDEN)
      case _ => new FakeResponse(status = OK)
    }
  }
}

object FakeBruteForcePreventionWebServiceImpl {
  final val VrmAttempt1 = "ST05YYA"
  final val VrmAttempt2 = "ST05YYB"
  final val VrmLocked = "ST05YYC"
}