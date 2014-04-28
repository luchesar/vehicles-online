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

  def storeDealerDetailsInCache(model: EnterAddressManuallyModel, dealerName: String) = {
    val dealerAddress = AddressViewModel.from(model.addressAndPostcodeModel)
    val value = DealerDetailsModel(dealerName = dealerName, dealerAddress = dealerAddress)
    Cache.set(dealerDetailsCacheKey, value)
    Logger.debug(s"EnterAddressManually stored data in cache: key = $dealerDetailsCacheKey, value = ${value}")
  }

  def storeTradeDetailsInCache(f: SetupTradeDetailsModel) = {
    Cache.set(SetupTradeDetailsCacheKey, f)
    Logger.debug(s"SetUpTradeDetails stored data in cache: key = $SetupTradeDetailsCacheKey, value = ${f}")
  }

  def storeBusinessChooseYourAddressModelInCache(value: BusinessChooseYourAddressModel) = {
    Cache.set(businessChooseYourAddressCacheKey, value)
    Logger.debug(s"BusinessChooseYourAddress stored BusinessChooseYourAddressModel in cache: key = $businessChooseYourAddressCacheKey, value = ${value}")
  }

  def storeDealerDetailsModelInCache(value: DealerDetailsModel) = {
    Cache.set(dealerDetailsCacheKey, value)
    Logger.debug(s"BusinessChooseYourAddress stored DealerDetailsModel in cache: key = $dealerDetailsCacheKey, value = ${value}")
  }

  def storeVehicleDetailsInCache(model: VehicleDetailsModel) = {
    Cache.set(vehicleLookupDetailsCacheKey, model)
    Logger.debug(s"VehicleLookup page - stored vehicle details object in cache: key = $vehicleLookupDetailsCacheKey, value = ${model}")
  }

  def storeVehicleLookupResponseCodeInCache(responseCode: String) = {
    Cache.set(vehicleLookupResponseCodeCacheKey, responseCode)
    Logger.debug(s"VehicleLookup page - stored vehicle lookup response code in cache: key = $vehicleLookupResponseCodeCacheKey, value = ${responseCode}")
  }

  def storeVehicleLookupFormModelInCache(model: VehicleLookupFormModel) = {
    Cache.set(vehicleLookupFormModelCacheKey, model)
    Logger.debug(s"VehicleLookup page - stored vehicle lookup form model details object in cache: key = $vehicleLookupFormModelCacheKey, value = ${model}")
  }

  def storeDisposeFormModelInCache(value: DisposeFormModel) = {
    Cache.set(disposeFormModelCacheKey, value)
    Logger.debug(s"Dispose - stored disposeFromModel in cache: key = $disposeFormModelCacheKey, value = $value")
  }

  def storeDisposeTransactionIdInCache(value: String) = {
    Cache.set(disposeFormTransactionIdCacheKey, value)
    Logger.debug(s"Dispose - stored dispose transaction id in cache: key = $disposeFormTransactionIdCacheKey, value = $value")
  }

  def storeDisposeTransactionTimestampInCache(value: String) = {
    Cache.set(disposeFormTimestampIdCacheKey, value)
    Logger.debug(s"Dispose - stored dispose transaction timestamp in cache: key = $disposeFormTimestampIdCacheKey, value = $value")
  }

  def storeDisposeRegistrationNumberInCache(value: String) = {
    Cache.set(disposeFormRegistrationNumberCacheKey, value)
    Logger.debug(s"Dispose - stored dispose registration number in cache: key = $disposeFormRegistrationNumberCacheKey, value = $value")
  }

  def storeDisposeModelInCache(value: DisposeModel) = {
    Cache.set(disposeModelCacheKey, value)
    Logger.debug(s"Dispose - stored formModel in cache: key = $disposeModelCacheKey, value = $value")
  }

  def fetchDealerNameFromCache: Option[String] = {
    fetchTraderDetailsFromCache match {
      case Some(model) => Some(model.traderBusinessName)
      case None => None
    }
  }

  def fetchDealerDetailsFromCache: Option[DealerDetailsModel] = Cache.getAs[DealerDetailsModel](dealerDetailsCacheKey)

  def fetchDisposeFormModelFromCache: Option[DisposeFormModel] = Cache.getAs[DisposeFormModel](disposeFormModelCacheKey)

  def fetchVehicleDetailsFromCache: Option[VehicleDetailsModel] = Cache.getAs[VehicleDetailsModel](vehicleLookupDetailsCacheKey)

  def fetchTraderDetailsFromCache: Option[SetupTradeDetailsModel] = Cache.getAs[SetupTradeDetailsModel](SetupTradeDetailsCacheKey)

  def fetchVehicleLookupDetailsFromCache: Option[VehicleLookupFormModel] = Cache.getAs[VehicleLookupFormModel](vehicleLookupFormModelCacheKey)

  def fetchVehicleLookupResponseCodeFromCache: Option[String] = Cache.getAs[String](vehicleLookupResponseCodeCacheKey)

  def fetchBusinessChooseYourAddressModelFromCache: Option[BusinessChooseYourAddressModel] = Cache.getAs[BusinessChooseYourAddressModel](businessChooseYourAddressCacheKey)

  def fetchDisposeTransactionIdFromCache: Option[String] = Cache.getAs[String](disposeFormTransactionIdCacheKey)

  def fetchDisposeTransactionTimestampInCache: Option[String] = Cache.getAs[String](disposeFormTimestampIdCacheKey)

  def fetchDisposeRegistrationNumberFromCache: Option[String] = Cache.getAs[String](disposeFormRegistrationNumberCacheKey)

  def clearVehicleLookupResponseCodeFromCache() = Cache.remove(vehicleLookupResponseCodeCacheKey)
}
