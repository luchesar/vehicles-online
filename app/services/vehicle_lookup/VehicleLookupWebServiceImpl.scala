package services.vehicle_lookup

import scala.concurrent.Future
import play.api.libs.ws.{WS, Response}
import models.domain.disposal_of_vehicle.VehicleDetailsRequest
import play.api.libs.json.Json
import utils.helpers.Config
import play.api.Logger
import models.domain.disposal_of_vehicle.VehicleDetailsRequest.vehicleDetailsRequest

class VehicleLookupWebServiceImpl extends VehicleLookupWebService {
  val endPoint = s"${Config.microServiceBaseUrl}/vehicles/lookup/v1"

  override def callVehicleLookupService(request: VehicleDetailsRequest): Future[Response] = {
    Logger.debug(s"Calling vehicle lookup micro service on ${endPoint} with request object: $request...")
    WS.url(endPoint).post(Json.toJson(request))
  }
}
