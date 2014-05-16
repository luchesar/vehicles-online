package mappings.disposal_of_vehicle

import mappings.disposal_of_vehicle.SetupTradeDetails._
import mappings.disposal_of_vehicle.TraderDetails._
import mappings.disposal_of_vehicle.BusinessChooseYourAddress._
import mappings.disposal_of_vehicle.VehicleLookup._
import mappings.disposal_of_vehicle.Dispose._

object RelatedCacheKeys {
  final val FullSet = Seq(SetupTradeDetailsCacheKey,
    traderDetailsCacheKey,
    businessChooseYourAddressCacheKey,
    vehicleLookupDetailsCacheKey,
    vehicleLookupResponseCodeCacheKey,
    vehicleLookupFormModelCacheKey,
    disposeFormModelCacheKey,
    disposeFormTransactionIdCacheKey,
    disposeFormTimestampIdCacheKey,
    disposeFormRegistrationNumberCacheKey,
    disposeModelCacheKey)

  final val DisposeSet = Seq(vehicleLookupDetailsCacheKey,
    vehicleLookupResponseCodeCacheKey,
    vehicleLookupFormModelCacheKey,
    disposeFormModelCacheKey,
    disposeFormTransactionIdCacheKey,
    disposeFormTimestampIdCacheKey,
    disposeFormRegistrationNumberCacheKey,
    disposeModelCacheKey)

  final val TradeDetailsSet = Seq(SetupTradeDetailsCacheKey,
      traderDetailsCacheKey,
      businessChooseYourAddressCacheKey)
}
