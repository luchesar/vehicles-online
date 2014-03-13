package helpers.disposal_of_vehicle

import pages.disposal_of_vehicle.CacheSetup

object DisposeFailurePage {
  val url = "/disposal-of-vehicle/dispose-failure"
  val title = "Dispose a vehicle into the motor trade: failure"

  def cacheSetup () {
    CacheSetup.businessChooseYourAddress()
    VehicleLookupPage.setupVehicleDetailsModelCache()
    DisposePage.setupDisposeFormModelCache()
    DisposePage.setupDisposeTransactionIdCache
    DisposePage.setupRegistrationNumberCache()
  }
}
