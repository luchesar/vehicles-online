package helpers.disposal_of_vehicle

import pages.disposal_of_vehicle.CacheSetup

object VehicleLookupFailurePage {
  val url = "/disposal-of-vehicle/vehicle-lookup-failure"
  val title = "Dispose a vehicle into the motor trade: vehicle lookup failure"

  def cacheSetup () {
    CacheSetup.businessChooseYourAddress()
    VehicleLookupPage.setupVehicleLookupFormModelCache()
  }
}

