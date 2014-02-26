package helpers.disposal_of_vehicle

import play.api.test.TestBrowser

object DisposeFailurePage {
  val url = "/disposal-of-vehicle/dispose-failure"
  val title = "Dispose a vehicle into the motor trade: failure"

  def cacheSetupHappyPath () {
    SetUpTradeDetailsPage.setupCache()
    BusinessChooseYourAddressPage.setupCache()
    VehicleLookupPage.setupCache()
    DisposePage.setupCache()
  }
}
