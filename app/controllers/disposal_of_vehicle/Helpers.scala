package controllers.disposal_of_vehicle

import play.api.cache.Cache
import play.api.Play.current

object Helpers {
  def fetchDealerNameFromCache(): Option[String] = {
    val key = mappings.disposal_of_vehicle.SetupTradeDetails.traderBusinessNameId
    Cache.getAs[String](key)
  }
}
