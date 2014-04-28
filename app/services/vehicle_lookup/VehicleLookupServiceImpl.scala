package services.vehicle_lookup

import play.api.Logger
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import models.domain.disposal_of_vehicle._
import models.domain.disposal_of_vehicle.VehicleDetailsResponse
import javax.inject.Inject
import models.domain.disposal_of_vehicle.VehicleDetailsResponse.vehicleDetailsResponse
import play.api.http.Status

class VehicleLookupServiceImpl @Inject()(ws: VehicleLookupWebService) extends VehicleLookupService {

  override def invoke(cmd: VehicleDetailsRequest): (Future[(Int, Option[VehicleDetailsResponse])]) = {
    ws.callVehicleLookupService(cmd).map {
      resp =>
        Logger.debug(s"Http response code from vehicle lookup micro-service was: ${resp.status}")
      if (resp.status == Status.OK) (resp.status, Some(resp.json.as[VehicleDetailsResponse]))
      else (resp.status, None)
    }
  }
}

