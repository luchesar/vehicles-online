package mappings.disposal_of_vehicle

import mappings.disposal_of_vehicle.SetupTradeDetails._
import mappings.disposal_of_vehicle.TraderDetails._
import mappings.disposal_of_vehicle.BusinessChooseYourAddress._
import mappings.disposal_of_vehicle.VehicleLookup._
import mappings.disposal_of_vehicle.Dispose._

object RelatedCacheKeys {
  final val SeenCookieMessageKey = "seen_cookie_message"

  final val DisposeSet = Set(VehicleLookupDetailsCacheKey,
    VehicleLookupResponseCodeCacheKey,
    VehicleLookupFormModelCacheKey,
    DisposeFormModelCacheKey,
    DisposeFormTransactionIdCacheKey,
    DisposeFormTimestampIdCacheKey,
    DisposeFormRegistrationNumberCacheKey,
    DisposeModelCacheKey)

  final val TradeDetailsSet = Set(SetupTradeDetailsCacheKey,
      TraderDetailsCacheKey,
      BusinessChooseYourAddressCacheKey)

  final val FullSet = TradeDetailsSet ++ DisposeSet
}
