package controllers.disposal_of_vehicle

import play.api.cache.Cache
import play.api.Play.current
import models.domain.disposal_of_vehicle._
import models.domain.disposal_of_vehicle.DealerDetailsModel
import models.domain.disposal_of_vehicle.DisposeFormModel
import scala.Some
import models.domain.disposal_of_vehicle.VehicleLookupFormModel
import models.domain.disposal_of_vehicle.SetupTradeDetailsModel
import models.domain.disposal_of_vehicle.VehicleDetailsModel
import play.api.Logger
import mappings.disposal_of_vehicle.DealerDetails

object Helpers {
  def fetchDealerNameFromCache: Option[String] = {
    fetchTraderDetailsFromCache match {
      case Some(model) => Some(model.traderBusinessName)
      case None => None
    }
  }

  def storeDealerDetailsModelInCache(value: DealerDetailsModel) = {
    val key = DealerDetails.cacheKey
    play.api.cache.Cache.set(key, value)
    Logger.debug(s"BusinessChooseYourAddress stored DealerDetailsModel in cache: key = $key, value = ${value}")
  }

  def fetchDealerDetailsFromCache: Option[DealerDetailsModel] = {
    val key = mappings.disposal_of_vehicle.DealerDetails.cacheKey
    Cache.getAs[DealerDetailsModel](key)
  }

  def fetchDisposeFormModelFromCache: Option[DisposeFormModel] = {
    val key = mappings.disposal_of_vehicle.Dispose.cacheKey
    Cache.getAs[DisposeFormModel](key)
  }

  def fetchVehicleDetailsFromCache: Option[VehicleDetailsModel] = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.cacheKey
    Cache.getAs[VehicleDetailsModel](key)
  }

  def fetchTraderDetailsFromCache: Option[SetupTradeDetailsModel] = {
    val key = mappings.disposal_of_vehicle.SetupTradeDetails.cacheKey
    Cache.getAs[SetupTradeDetailsModel](key)
  }

  def fetchVehicleLookupDetailsFromCache: Option[VehicleLookupFormModel] = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.cacheVehicleLookupFormModelKey
    Cache.getAs[VehicleLookupFormModel](key)
  }


  def storeBusinessChooseYourAddressModelInCache(value: BusinessChooseYourAddressModel) = {
    val key = mappings.disposal_of_vehicle.BusinessChooseYourAddress.cacheKey
    play.api.cache.Cache.set(key, value)
    Logger.debug(s"BusinessChooseYourAddress stored BusinessChooseYourAddressModel in cache: key = $key, value = ${value}")
  }

  def fetchBusinessChooseYourAddressModelFromCache: Option[BusinessChooseYourAddressModel] = {
    val key = mappings.disposal_of_vehicle.BusinessChooseYourAddress.cacheKey
    Cache.getAs[BusinessChooseYourAddressModel](key)
  }
}
