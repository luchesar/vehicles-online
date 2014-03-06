package helpers.disposal_of_vehicle

object VehicleLookupFailurePage {
  val url = "/disposal-of-vehicle/vehicle-lookup-failure"
  val title = "Dispose a vehicle into the motor trade: vehicle lookup failure"

  def cacheSetup () {
    BusinessChooseYourAddressPage.setupCache()
    VehicleLookupPage.setupVehicleLookupFormModelCache()
  }
}

