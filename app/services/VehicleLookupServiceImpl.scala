package services

import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.Logger
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import utils.helpers.Config
import models.domain.disposal_of_vehicle._
import models.domain.disposal_of_vehicle.VehicleDetailsResponse

class VehicleLookupServiceImpl() extends VehicleLookupService {
  import models.domain.disposal_of_vehicle.VehicleDetailsResponse.vehicleDetailsResponse
  import models.domain.disposal_of_vehicle.VehicleDetailsRequest.vehicleDetailsRequest

  override def invoke(cmd: VehicleDetailsRequest): Future[VehicleDetailsResponse] = {
    val endPoint = s"${Config.microServiceBaseUrl}/vehicles/lookup/v1"
    Logger.debug(s"Calling vehicle lookup micro service on ${endPoint} with request object: ${cmd}...")
    val futureOfResponse = WS.url(endPoint).post(Json.toJson(cmd))

    futureOfResponse.map {
      resp =>
        Logger.debug(s"Http response code from vehicle lookup micro service was: ${resp.status}")
        resp.json.as[VehicleDetailsResponse]
    }
  }
}

