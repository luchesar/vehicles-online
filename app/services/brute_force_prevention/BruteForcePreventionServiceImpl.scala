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
  override def vrmLookupPermitted(vrm: String): Future[Option[(Boolean, BruteForcePreventionResponse)]] =
    if (Config.bruteForcePreventionEnabled) {
      // TODO US270 this is temporary until we all developers have Redis setup locally.
      ws.callBruteForce(vrm).map {
        resp =>
          resp.status match {
            case play.api.http.Status.OK =>
              val bruteForcePreventionResponse: Option[BruteForcePreventionResponse] = Json.fromJson[BruteForcePreventionResponse](resp.json).asOpt
              bruteForcePreventionResponse match {
                case Some(model) =>
                  Some((true, model))
                case _ =>
                  Logger.error(s"Brute force prevention service returned invalid Json: ${resp.json}")
                  None
              }
            case play.api.http.Status.FORBIDDEN => Some((false, BruteForcePreventionResponse(attempts = 0, maxAttempts = 0)))
            case _ =>
              Logger.error(s"Brute force prevention service returned status: ${resp.status}")
              None
          }
      }.recover {
        case e: Throwable =>
          Logger.error(s"Brute force prevention service error: $e")
          None
      }
    }
    else Future {
      Some((true, BruteForcePreventionResponse(attempts = 0, maxAttempts = 0)))
    }
}
