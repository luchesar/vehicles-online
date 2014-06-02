package mappings.disposal_of_vehicle

import mappings.disposal_of_vehicle.SetupTradeDetails._
import mappings.disposal_of_vehicle.TraderDetails._
import mappings.disposal_of_vehicle.BusinessChooseYourAddress._
import mappings.disposal_of_vehicle.VehicleLookup._
import mappings.disposal_of_vehicle.Dispose._
import mappings.disposal_of_vehicle.EnterAddressManually._
import models.domain.disposal_of_vehicle.BruteForcePreventionViewModel._

object RelatedCacheKeys {
  final val SeenCookieMessageKey = "seen_cookie_message"

  val DisposeSet = Set(
    BruteForcePreventionViewModelCacheKey,
    VehicleLookupDetailsCacheKey,
    VehicleLookupResponseCodeCacheKey,
    VehicleLookupFormModelCacheKey,
    DisposeFormModelCacheKey,
    DisposeFormTransactionIdCacheKey,
    DisposeFormTimestampIdCacheKey,
    DisposeFormRegistrationNumberCacheKey,
    DisposeModelCacheKey)

  val TradeDetailsSet = Set(SetupTradeDetailsCacheKey,
      TraderDetailsCacheKey,
      BusinessChooseYourAddressCacheKey,
      EnterAddressManuallyCacheKey)

  val FullSet = TradeDetailsSet ++ DisposeSet
}
