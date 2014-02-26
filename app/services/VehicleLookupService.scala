package services

import scala.concurrent.Future
import models.domain.disposal_of_vehicle.{VehicleLookupFormModel, VehicleDetailsResponse, VehicleDetailsModel}

trait VehicleLookupService {
  def invoke(cmd: VehicleLookupFormModel): Future[VehicleDetailsResponse]
}
