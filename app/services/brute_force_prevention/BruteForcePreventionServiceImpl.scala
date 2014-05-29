package services.brute_force_prevention

import javax.inject.Inject
import play.api.Logger
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

final class BruteForcePreventionServiceImpl @Inject()(ws: BruteForcePreventionWebService) extends BruteForceService {
  override def vrmLookupPermitted(vrm: String): Future[Boolean] = {
    ws.callBruteForce(vrm).map {
      resp =>
        Logger.debug(s"Http response code from Brute force prevention service was: ${resp.status}")
        resp.status == play.api.http.Status.OK
    }.recover {
      case e: Throwable =>
        Logger.error(s"Brute force prevention service error: $e")
        false
    }
  }
}
