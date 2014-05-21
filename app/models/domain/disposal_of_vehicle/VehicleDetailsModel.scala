package models.domain.disposal_of_vehicle

import play.api.libs.json.Json
import mappings.disposal_of_vehicle.VehicleLookup.VehicleLookupDetailsCacheKey
import models.domain.common.CacheKey

case class VehicleDetailsModel(registrationNumber: String, vehicleMake: String, vehicleModel: String)

object VehicleDetailsModel {
  // Create a VehicleDetailsModel from the given VehicleDetailsDto. We do this in order get the data out of the response from micro-service call
  def fromDto(model: VehicleDetailsDto) = VehicleDetailsModel(registrationNumber = model.registrationNumber, vehicleMake = model.vehicleMake,
    vehicleModel = model.vehicleModel)

  implicit val JsonFormat = Json.format[VehicleDetailsModel]
  implicit val cacheKey = CacheKey[VehicleDetailsModel](VehicleLookupDetailsCacheKey)
}