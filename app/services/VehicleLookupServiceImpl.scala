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

class VehicleLookupServiceImpl() extends VehicleLookupService {
  implicit val addressViewModel = Json.format[AddressViewModel]
  implicit val vehicleDetailsModel = Json.format[VehicleDetailsModel]
  implicit val vehicleDetailsResponse = Json.format[VehicleDetailsResponse]
  implicit val vehicleDetailsRequest = Json.writes[VehicleDetailsRequest]

  override def invoke(cmd: VehicleDetailsRequest): Future[VehicleDetailsResponse] = {
    val endPoint = s"${Config.microServiceBaseUrl}/vehicles/vehicle-lookup"
    Logger.debug(s"Calling vehicle lookup micro service on ${endPoint} with request object: ${cmd}...")
    val futureOfResponse = WS.url(endPoint).post(Json.toJson(cmd))

    futureOfResponse.map { resp =>
      Logger.debug(s"Http response code from vehicle lookup micro service was: ${resp.status}")
      resp.json.as[VehicleDetailsResponse]
    }
  }
}

