package models.domain.disposal_of_vehicle

import play.api.libs.json.Json
import models.domain.common.CacheKey
import mappings.disposal_of_vehicle.VehicleLookup.vehicleLookupFormModelCacheKey

case class VehicleLookupFormModel(referenceNumber: String,
                                  registrationNumber: String)

object VehicleLookupFormModel {
  implicit val vehicleLookupFormModelFormat = Json.format[VehicleLookupFormModel]
  implicit val cacheKey = CacheKey[VehicleLookupFormModel](vehicleLookupFormModelCacheKey)
}