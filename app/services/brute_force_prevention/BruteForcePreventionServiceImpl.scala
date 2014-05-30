package services.brute_force_prevention

import javax.inject.Inject
import play.api.Logger
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import utils.helpers.Config
import models.domain.common.BruteForcePreventionResponse
import models.domain.common.BruteForcePreventionResponse.JsonFormat
import play.api.libs.json.Json


final class BruteForcePreventionServiceImpl @Inject()(ws: BruteForcePreventionWebService) extends BruteForcePreventionService {
  override def vrmLookupPermitted(vrm: String): Future[(Boolean, Int, Int)] =
    if (Config.bruteForcePreventionEnabled) {
      // TODO US270 this is temporary until we all developers have Redis setup locally.
      ws.callBruteForce(vrm).map {
        resp =>
          Logger.debug(s"Http response code from Brute force prevention service was: ${resp.status}")

          //(resp.status == play.api.http.Status.OK, 0, 3)
          val bruteForcePreventionResponse: Option[BruteForcePreventionResponse] = Json.fromJson[BruteForcePreventionResponse](resp.json).asOpt
          bruteForcePreventionResponse match {
            case Some(model) =>
              (resp.status == play.api.http.Status.OK, model.attempts, model.maxAttempts)
            case _ =>
              Logger.error(s"Brute force prevention service returned an unexpected type of Json: ${resp.json}")
              (false, 0, 0)
          }
      }.recover {
        case e: Throwable =>
          Logger.error(s"Brute force prevention service error: $e")
          (false, 0, 0)
      }
    }
    else Future {
      (true, 0, 0)
    }
}
