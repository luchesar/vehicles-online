package models.domain.disposal_of_vehicle

import models.domain.common.{CacheKey, BruteForcePreventionResponse}
import play.api.libs.json.Json

final case class BruteForcePreventionViewModel(permitted: Boolean, attempts: Int, maxAttempts: Int)

object BruteForcePreventionViewModel {
  def fromResponse(permitted: Boolean, response: BruteForcePreventionResponse): BruteForcePreventionViewModel =
    BruteForcePreventionViewModel(permitted,
      attempts = response.attempts + 1,
      maxAttempts = response.maxAttempts + 1
    )

  implicit val JsonFormat = Json.format[BruteForcePreventionViewModel]
  final val BruteForcePreventionViewModelCacheKey = "bruteForcePreventionViewModel"
  implicit val Key = CacheKey[BruteForcePreventionViewModel](BruteForcePreventionViewModelCacheKey)
}

