package services.brute_force_prevention

import javax.inject.Inject
import play.api.Logger
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import utils.helpers.Config
import models.domain.common.BruteForcePreventionResponse
import models.domain.common.BruteForcePreventionResponse.JsonFormat
import play.api.libs.json.Json
import models.domain.disposal_of_vehicle.BruteForcePreventionViewModel
import services.DateService

final class BruteForcePreventionServiceImpl @Inject()(config: Config, ws: BruteForcePreventionWebService, dateService: DateService) extends BruteForcePreventionService {
  private val maxAttempts: Int = config.bruteForcePreventionMaxAttemptsHeader.toInt

  override def isVrmLookupPermitted(vrm: String): Future[Option[BruteForcePreventionViewModel]] =
  // TODO US270 this if-statement is a temporary feature toggle until all developers have Redis setup locally.
    if (config.isBruteForcePreventionEnabled) {
      ws.callBruteForce(vrm).map {
        resp =>
          def permitted = {
            val bruteForcePreventionResponse: Option[BruteForcePreventionResponse] = Json.fromJson[BruteForcePreventionResponse](resp.json).asOpt
            bruteForcePreventionResponse match {
              case Some(model) => Some(BruteForcePreventionViewModel.fromResponse(permitted = true, model, dateService, maxAttempts = maxAttempts))
              case _ =>
                Logger.error(s"Brute force prevention service returned invalid Json: ${resp.json}")
                None
            }
          }
          def notPermitted = Some(BruteForcePreventionViewModel.fromResponse(permitted = false, BruteForcePreventionResponse(attempts = 0), dateService, maxAttempts = maxAttempts))
          def unknownPermission = {
            Logger.error(s"Brute force prevention service returned status: ${resp.status}")
            None
          }
          resp.status match {
            case play.api.http.Status.OK => permitted
            case play.api.http.Status.FORBIDDEN => notPermitted
            case _ => unknownPermission
          }
      }.recover {
        case e: Throwable =>
          Logger.error(s"Brute force prevention service throws: ${e.getStackTraceString}")
          None
      }
    }
    else Future {
      Some(BruteForcePreventionViewModel.fromResponse(permitted = true, BruteForcePreventionResponse(attempts = 0), dateService, maxAttempts = maxAttempts))
    }
}