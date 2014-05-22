package models.domain.disposal_of_vehicle

import play.api.libs.json.Json
import models.domain.common.CacheKey
import mappings.disposal_of_vehicle.VehicleLookup.VehicleLookupFormModelCacheKey

final case class VehicleLookupFormModel(referenceNumber: String,
                                  registrationNumber: String)

object VehicleLookupFormModel {
  implicit val JsonFormat = Json.format[VehicleLookupFormModel]
  implicit val Key = CacheKey[VehicleLookupFormModel](VehicleLookupFormModelCacheKey)
}