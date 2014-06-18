package services.vehicle_lookup

import scala.concurrent.Future
import play.api.libs.ws.Response
import models.domain.disposal_of_vehicle.VehicleDetailsRequest

trait VehicleLookupWebService {
  def callVehicleLookupService(request: VehicleDetailsRequest, trackingId: String): Future[Response]
}

