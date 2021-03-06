package models.domain.disposal_of_vehicle

import models.domain.common.{BruteForcePreventionResponse, CacheKey}
import play.api.libs.json.Json
import services.DateService

final case class BruteForcePreventionViewModel(permitted: Boolean,
                                               attempts: Int,
                                               maxAttempts: Int,
                                               dateTimeISOChronology: String)

object BruteForcePreventionViewModel {
  implicit final val JsonFormat = Json.format[BruteForcePreventionViewModel]
  implicit final val Key = CacheKey[BruteForcePreventionViewModel](BruteForcePreventionViewModelCacheKey)
  final val BruteForcePreventionViewModelCacheKey = "bruteForcePreventionViewModel"

  def fromResponse(permitted: Boolean,
                   response: BruteForcePreventionResponse,
                   dateService: DateService,
                   maxAttempts: Int): BruteForcePreventionViewModel = {
    BruteForcePreventionViewModel(permitted,
      attempts = response.attempts + 1,
      maxAttempts = maxAttempts,
      dateTimeISOChronology = dateService.dateTimeISOChronology // Save the time we locked in case we need to display it on a page e.g. vrm-locked page.
    )
  }
}