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

  def storeTradeDetailsInCache(f: SetupTradeDetailsModel) = {
    val key = mappings.disposal_of_vehicle.SetupTradeDetails.cacheKey
    play.api.cache.Cache.set(key, f)
    Logger.debug(s"SetUpTradeDetails stored data in cache: key = $key, value = ${f}")
  }

  def storeBusinessChooseYourAddressModelInCache(value: BusinessChooseYourAddressModel) = {
    val key = mappings.disposal_of_vehicle.BusinessChooseYourAddress.cacheKey
    play.api.cache.Cache.set(key, value)
    Logger.debug(s"BusinessChooseYourAddress stored BusinessChooseYourAddressModel in cache: key = $key, value = ${value}")
  }

  def storeDealerDetailsInCache(model: EnterAddressManuallyModel, dealerName: String) = {
    val dealerAddress = AddressViewModel.from(model.addressAndPostcodeModel)
    val key = DealerDetails.cacheKey
    val value = DealerDetailsModel(dealerName = dealerName, dealerAddress = dealerAddress)
    play.api.cache.Cache.set(key, value)
    Logger.debug(s"EnterAddressManually stored data in cache: key = $key, value = ${value}")
  }

  def storeDealerDetailsModelInCache(value: DealerDetailsModel) = {
    val key = DealerDetails.cacheKey
    play.api.cache.Cache.set(key, value)
    Logger.debug(s"BusinessChooseYourAddress stored DealerDetailsModel in cache: key = $key, value = ${value}")
  }

  def storeVehicleDetailsInCache(model: VehicleDetailsModel) = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.cacheKey
    play.api.cache.Cache.set(key, model)
    Logger.debug(s"VehicleLookup page - stored vehicle details object in cache: key = $key, value = ${model}")
  }

  def storeVehicleLookupFormModelInCache(model: VehicleLookupFormModel) = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.cacheVehicleLookupFormModelKey
    play.api.cache.Cache.set(key, model)
    Logger.debug(s"VehicleLookup page - stored vehicle lookup form model details object in cache: key = $key, value = ${model}")
  }

  def storeDisposeFormModelInCache(value: DisposeFormModel) = {
    val key = mappings.disposal_of_vehicle.Dispose.DisposeFormModelCacheKey
    play.api.cache.Cache.set(key, value)
    Logger.debug(s"Dispose - stored disposeFromModel in cache: key = $key, value = $value")
  }

  def storeDisposeTransactionIdInCache(value: String) = {
    val key = mappings.disposal_of_vehicle.Dispose.DisposeFormTransactionIdCacheKey
    play.api.cache.Cache.set(key, value)
    Logger.debug(s"Dispose - stored dispose transaction id in cache: key = $key, value = $value")
  }

  def storeDisposeModelInCache(value: DisposeModel) = {
    val key = mappings.disposal_of_vehicle.Dispose.DisposeModelCacheKey
    play.api.cache.Cache.set(key, value)
    Logger.debug(s"Dispose - stored disposeModel in cache: key = $key, value = $value")
  }


  def fetchDealerNameFromCache: Option[String] = {
    fetchTraderDetailsFromCache match {
      case Some(model) => Some(model.traderBusinessName)
      case None => None
    }
  }

  def fetchDealerDetailsFromCache: Option[DealerDetailsModel] = {
    val key = mappings.disposal_of_vehicle.DealerDetails.cacheKey
    Cache.getAs[DealerDetailsModel](key)
  }

  def fetchDisposeFormModelFromCache: Option[DisposeFormModel] = {
    val key = mappings.disposal_of_vehicle.Dispose.DisposeFormModelCacheKey
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

  def fetchBusinessChooseYourAddressModelFromCache: Option[BusinessChooseYourAddressModel] = {
    val key = mappings.disposal_of_vehicle.BusinessChooseYourAddress.cacheKey
    Cache.getAs[BusinessChooseYourAddressModel](key)
  }

  def fetchDisposeTransactionIdFromCache: Option[String] = {
    val key = mappings.disposal_of_vehicle.Dispose.DisposeFormTransactionIdCacheKey
    Cache.getAs[String](key)
  }
}
