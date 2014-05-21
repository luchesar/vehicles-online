package models.domain.disposal_of_vehicle

import models.DayMonthYear
import play.api.libs.json.Json
import mappings.disposal_of_vehicle.Dispose._
import models.domain.common.CacheKey

case class DisposeFormModel(mileage: Option[Int], dateOfDisposal: DayMonthYear, consent: String, lossOfRegistrationConsent: String)

object DisposeFormModel {
  implicit val JsonFormat = Json.format[DisposeFormModel]
  implicit val cacheKey = CacheKey[DisposeFormModel](value = DisposeFormModelCacheKey)
}