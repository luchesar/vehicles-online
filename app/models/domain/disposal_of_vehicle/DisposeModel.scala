package models.domain.disposal_of_vehicle

import models.DayMonthYear
import play.api.libs.json.Json
import mappings.disposal_of_vehicle.Dispose._
import models.domain.common.CacheKey

case class DisposeModel(referenceNumber: String,
                        registrationNumber: String,
                        dateOfDisposal: DayMonthYear,
                        mileage: Option[Int])

object DisposeModel {
  implicit val disposeModelFormat = Json.format[DisposeModel]
  implicit val cacheKey = CacheKey[DisposeModel](value = disposeModelCacheKey)
}