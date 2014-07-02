package services.vehicle_lookup

import models.domain.disposal_of_vehicle.VehicleDetailsRequest
import play.api.libs.ws.Response
import scala.concurrent.Future

trait VehicleLookupWebService {
  def callVehicleLookupService(request: VehicleDetailsRequest, trackingId: String): Future[Response]
}