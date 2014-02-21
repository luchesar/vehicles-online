package controllers.disposal_of_vehicle

import play.api.cache.Cache
import play.api.Play.current
import models.domain.disposal_of_vehicle.{SetupTradeDetailsModel, DisposeFormModel, VehicleDetailsModel, DealerDetailsModel}
import models.DayMonthYear
import play.api.Logger

object Helpers {
  def fetchDealerNameFromCache(): Option[String] = {
    fetchTraderDetailsFromCache match {
      case Some(model) => Some(model.traderBusinessName)
      case None => None
    }
  }

  def fetchDealerDetailsFromCache: Option[DealerDetailsModel] = {
    val key = mappings.disposal_of_vehicle.DealerDetails.cacheKey
    Cache.getAs[DealerDetailsModel](key)
  }

  def fetchDisposeFormModelFromCache(): Option[DisposeFormModel] = {
    val key = mappings.disposal_of_vehicle.Dispose.cacheKey
    Cache.getAs[DisposeFormModel](key)
  }

  def fetchVehicleDetailsFromCache(): Option[VehicleDetailsModel] = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.cacheKey
    Cache.getAs[VehicleDetailsModel](key)
  }

  def fetchTraderDetailsFromCache: Option[SetupTradeDetailsModel] = {
    val key = mappings.disposal_of_vehicle.SetupTradeDetails.cacheKey
    Cache.getAs[SetupTradeDetailsModel](key)
  }
}
