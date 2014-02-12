package controllers.disposal_of_vehicle

import play.api.cache.Cache
import play.api.Play.current
import models.domain.disposal_of_vehicle.DealerDetailsModel
import models.DayMonthYear

object Helpers {
  def fetchDealerNameFromCache(): Option[String] = {
    val key = mappings.disposal_of_vehicle.SetupTradeDetails.dealerNameId
    Cache.getAs[String](key)
  }

  def fetchDealerDetailsFromCache: Option[DealerDetailsModel] = {
    val key = mappings.disposal_of_vehicle.DealerDetails.cacheKey
    Cache.getAs[DealerDetailsModel](key)
  }

  def fetchDisposalDate(): Option[DayMonthYear] = {
    val key = mappings.disposal_of_vehicle.Dispose.cacheKey
    Cache.getAs[DayMonthYear](key)
  }
}
