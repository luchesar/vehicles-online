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
import models.domain.disposal_of_vehicle.BruteForcePreventionViewModel

final class BruteForcePreventionServiceImpl @Inject()(config: Config, ws: BruteForcePreventionWebService) extends BruteForcePreventionService {
  override def isVrmLookupPermitted(vrm: String): Future[Option[BruteForcePreventionViewModel]] =
  // TODO US270 this if-statement is a temporary feature toggle until all developers have Redis setup locally.
    if (config.bruteForcePreventionEnabled) {
      ws.callBruteForce(vrm).map {
        resp =>
          def permitted(resp: Response) = {
            val bruteForcePreventionResponse: Option[BruteForcePreventionResponse] = Json.fromJson[BruteForcePreventionResponse](resp.json).asOpt
            bruteForcePreventionResponse match {
              case Some(model) => Some(BruteForcePreventionViewModel.fromResponse(permitted = true, model))
              case _ =>
                Logger.error(s"Brute force prevention service returned invalid Json: ${resp.json}")
                None
            }
          }
          def notPermitted = Some(BruteForcePreventionViewModel.fromResponse(permitted = false, BruteForcePreventionResponse(attempts = 0, maxAttempts = 0)))
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
          Logger.error(s"Brute force prevention service throws: ${e.getStackTraceString}")
          None
      }
    }
    else Future {
      Some(BruteForcePreventionViewModel.fromResponse(permitted = true, BruteForcePreventionResponse(attempts = 0, maxAttempts = 0)))
    }
}
