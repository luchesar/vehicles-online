package helpers.disposal_of_vehicle

import play.api.test.TestBrowser

object VehicleLookupFailurePage {
  val url = "/disposal-of-vehicle/vehicle-lookup-failure"
  val title = "Dispose a vehicle into the motor trade: vehicle lookup failure"

  def cacheSetupHappyPath (browser: TestBrowser) {
    BusinessChooseYourAddressPage.setupCache
    VehicleLookupPage.setupCache
  }
}

