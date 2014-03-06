package services

import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.Logger
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import utils.helpers.Config
import models.domain.disposal_of_vehicle._
import models.domain.disposal_of_vehicle.VehicleDetailsResponse
import models.domain.disposal_of_vehicle.VehicleLookupFormModel
import models.domain.disposal_of_vehicle.VehicleDetailsModel

class DisposeServiceImpl() extends DisposeService {
  implicit val disposeResponse = Json.reads[DisposeResponse]
  implicit val disposeModel = Json.writes[DisposeModel]

  override def invoke(cmd: DisposeModel): Future[DisposeResponse] = {
    val endPoint = s"${Config.microServiceBaseUrl}/dispose"
    Logger.debug(s"Calling dispose vehicle micro service on ${endPoint} with request object: ${cmd}...")
    val futureOfResponse = WS.url(endPoint).post(Json.toJson(cmd))

    futureOfResponse.map {
      resp =>
        Logger.debug(s"Http response code from dispose vehicle micro service was: ${resp.status}")
        resp.json.as[DisposeResponse]
    }
  }
}

