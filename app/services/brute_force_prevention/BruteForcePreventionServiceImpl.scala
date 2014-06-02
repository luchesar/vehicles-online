package services.brute_force_prevention

import javax.inject.Inject
import play.api.Logger
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import utils.helpers.Config
import models.domain.common.BruteForcePreventionResponse
import models.domain.common.BruteForcePreventionResponse.JsonFormat
import play.api.libs.json.Json
import play.api.libs.ws.Response

final class BruteForcePreventionServiceImpl @Inject()(ws: BruteForcePreventionWebService) extends BruteForcePreventionService {
  override def vrmLookupPermitted(vrm: String): Future[Option[(Boolean, BruteForcePreventionResponse)]] =
  // TODO US270 this if-statement is a temporary feature toggle until all developers have Redis setup locally.
    if (Config.bruteForcePreventionEnabled) {
      ws.callBruteForce(vrm).map {
        resp =>
          def permitted(resp: Response) = {
            val bruteForcePreventionResponse: Option[BruteForcePreventionResponse] = Json.fromJson[BruteForcePreventionResponse](resp.json).asOpt
            bruteForcePreventionResponse match {
              case Some(model) => Some((true, model))
              case _ =>
                Logger.error(s"Brute force prevention service returned invalid Json: ${resp.json}")
                None
            }
          }
          def notPermitted = Some((false, BruteForcePreventionResponse(attempts = 0, maxAttempts = 0)))
          def unknownPermission(resp: Response) = {
            Logger.error(s"Brute force prevention service returned status: ${resp.status}")
            None
          }

          resp.status match {
            case play.api.http.Status.OK => permitted(resp)
            case play.api.http.Status.FORBIDDEN => notPermitted
            case _ => unknownPermission(resp)
          }
      }.recover {
        case e: Throwable =>
          Logger.error(s"Brute force prevention service error: ${e.getStackTraceString}")
          None
      }
    }
    else Future {
      Some((true, BruteForcePreventionResponse(attempts = 0, maxAttempts = 0)))
    }
}
