package helpers.disposal_of_vehicle

object VehicleLookupFailurePage {
  val url = "/disposal-of-vehicle/vehicle-lookup-failure"
  val title = "Dispose a vehicle into the motor trade: vehicle lookup failure"

  def cacheSetupHappyPath () {
    BusinessChooseYourAddressPage.setupCache
    VehicleLookupPage.setupVehicleLookupFormModelCache()
  }
}

