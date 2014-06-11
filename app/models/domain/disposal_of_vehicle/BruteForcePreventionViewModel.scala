package models.domain.disposal_of_vehicle

import models.domain.common.{CacheKey, BruteForcePreventionResponse}
import play.api.libs.json.Json
import services.DateService

final case class BruteForcePreventionViewModel(permitted: Boolean, attempts: Int, maxAttempts: Int, dateTimeISOChronology: String)

object BruteForcePreventionViewModel {
  def fromResponse(permitted: Boolean, response: BruteForcePreventionResponse, dateService: DateService, maxAttempts: Int): BruteForcePreventionViewModel = {
    BruteForcePreventionViewModel(permitted,
      attempts = response.attempts + 1,
      maxAttempts = maxAttempts,
      dateTimeISOChronology = dateService.dateTimeISOChronology // Save the time we locked incase we need to display it on a page e.g. vrm-locked page.
    )
  }

  implicit val JsonFormat = Json.format[BruteForcePreventionViewModel]
  final val BruteForcePreventionViewModelCacheKey = "bruteForcePreventionViewModel"
  implicit val Key = CacheKey[BruteForcePreventionViewModel](BruteForcePreventionViewModelCacheKey)
}

