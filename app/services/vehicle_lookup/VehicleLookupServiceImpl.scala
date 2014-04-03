package services.vehicle_lookup

import play.api.Logger
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import models.domain.disposal_of_vehicle._
import models.domain.disposal_of_vehicle.VehicleDetailsResponse
import javax.inject.Inject
import models.domain.disposal_of_vehicle.VehicleDetailsResponse.vehicleDetailsResponse

class VehicleLookupServiceImpl @Inject()(ws: VehicleLookupWebService) extends VehicleLookupService {

  override def invoke(cmd: VehicleDetailsRequest): Future[VehicleDetailsResponse] = {
    ws.callVehicleLookupService(cmd).map {
      resp =>
        Logger.debug(s"Http response code from vehicle lookup micro service was: ${resp.status}")
        resp.json.as[VehicleDetailsResponse]
    }
  }
}

