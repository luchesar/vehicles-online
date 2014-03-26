package services

import scala.concurrent.Future
import models.domain.disposal_of_vehicle.{VehicleDetailsRequest, VehicleDetailsResponse}

trait VehicleLookupService {
  def invoke(cmd: VehicleDetailsRequest): Future[VehicleDetailsResponse]
}
