package models.domain.disposal_of_vehicle

import mappings.disposal_of_vehicle.VehicleLookup.VehicleLookupFormModelCacheKey
import models.domain.common.CacheKey
import play.api.libs.json.Json

final case class VehicleLookupFormModel(referenceNumber: String,
                                        registrationNumber: String)

object VehicleLookupFormModel {
  implicit val JsonFormat = Json.format[VehicleLookupFormModel]
  implicit val Key = CacheKey[VehicleLookupFormModel](VehicleLookupFormModelCacheKey)
}