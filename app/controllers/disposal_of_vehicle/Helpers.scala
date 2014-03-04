package controllers.disposal_of_vehicle

import play.api.cache.Cache
import play.api.Play.current
import play.api.Logger
import models.domain.disposal_of_vehicle._
import models.domain.disposal_of_vehicle.{DealerDetailsModel, DisposeFormModel, VehicleLookupFormModel, SetupTradeDetailsModel, VehicleDetailsModel}
import scala.Some
import mappings.disposal_of_vehicle.SetupTradeDetails._
import mappings.disposal_of_vehicle.BusinessChooseYourAddress._
import mappings.disposal_of_vehicle.DealerDetails._
import mappings.disposal_of_vehicle.Dispose._
import mappings.disposal_of_vehicle.VehicleLookup._

object Helpers {

  def storeTradeDetailsInCache(f: SetupTradeDetailsModel) = {
    val key = SetupTradeDetailsCacheKey
    Cache.set(key, f)
    Logger.debug(s"SetUpTradeDetails stored data in cache: key = $key, value = ${f}")
  }

  def storeBusinessChooseYourAddressModelInCache(value: BusinessChooseYourAddressModel) = {
    val key = businessChooseYourAddressCacheKey
    Cache.set(key, value)
    Logger.debug(s"BusinessChooseYourAddress stored BusinessChooseYourAddressModel in cache: key = $key, value = ${value}")
  }

  def storeDealerDetailsInCache(model: EnterAddressManuallyModel, dealerName: String) = {
    val key = dealerDetailsCacheKey
    val dealerAddress = AddressViewModel.from(model.addressAndPostcodeModel)
    val value = DealerDetailsModel(dealerName = dealerName, dealerAddress = dealerAddress)
    Cache.set(key, value)
    Logger.debug(s"EnterAddressManually stored data in cache: key = $key, value = ${value}")
  }

  def storeDealerDetailsModelInCache(value: DealerDetailsModel) = {
    val key = dealerDetailsCacheKey
    Cache.set(key, value)
    Logger.debug(s"BusinessChooseYourAddress stored DealerDetailsModel in cache: key = $key, value = ${value}")
  }

  def storeVehicleDetailsInCache(model: VehicleDetailsModel) = {
    val key = vehicleLookupDetailsCacheKey
    Cache.set(key, model)
    Logger.debug(s"VehicleLookup page - stored vehicle details object in cache: key = $key, value = ${model}")
  }

  def storeVehicleLookupFormModelInCache(model: VehicleLookupFormModel) = {
    val key = vehicleLookupFormModelCacheKey
    Cache.set(key, model)
    Logger.debug(s"VehicleLookup page - stored vehicle lookup form model details object in cache: key = $key, value = ${model}")
  }

  def storeDisposeFormModelInCache(value: DisposeFormModel) = {
    val key = disposeFormModelCacheKey
    Cache.set(key, value)
    Logger.debug(s"Dispose - stored disposeFromModel in cache: key = $key, value = $value")
  }

  def storeDisposeTransactionIdInCache(value: String) = {
    val key = disposeFormTransactionIdCacheKey
    Cache.set(key, value)
    Logger.debug(s"Dispose - stored dispose transaction id in cache: key = $key, value = $value")
  }

  def storeDisposeModelInCache(value: DisposeModel) = {
    val key = disposeModelCacheKey
    Cache.set(key, value)
    Logger.debug(s"Dispose - stored disposeModel in cache: key = $key, value = $value")
  }


  def fetchDealerNameFromCache: Option[String] = {
    fetchTraderDetailsFromCache match {
      case Some(model) => Some(model.traderBusinessName)
      case None => None
    }
  }

  def fetchDealerDetailsFromCache: Option[DealerDetailsModel] = {
    val key = dealerDetailsCacheKey
    Cache.getAs[DealerDetailsModel](key)
  }

  def fetchDisposeFormModelFromCache: Option[DisposeFormModel] = {
    val key = disposeFormModelCacheKey
    Cache.getAs[DisposeFormModel](key)
  }

  def fetchVehicleDetailsFromCache: Option[VehicleDetailsModel] = {
    val key = vehicleLookupDetailsCacheKey
    Cache.getAs[VehicleDetailsModel](key)
  }

  def fetchTraderDetailsFromCache: Option[SetupTradeDetailsModel] = {
    val key = SetupTradeDetailsCacheKey
    Cache.getAs[SetupTradeDetailsModel](key)
  }

  def fetchVehicleLookupDetailsFromCache: Option[VehicleLookupFormModel] = {
    val key = vehicleLookupFormModelCacheKey
    Cache.getAs[VehicleLookupFormModel](key)
  }

  def fetchBusinessChooseYourAddressModelFromCache: Option[BusinessChooseYourAddressModel] = {
    val key = businessChooseYourAddressCacheKey
    Cache.getAs[BusinessChooseYourAddressModel](key)
  }

  def fetchDisposeTransactionIdFromCache: Option[String] = {
    val key = disposeFormTransactionIdCacheKey
    Cache.getAs[String](key)
  }
}
