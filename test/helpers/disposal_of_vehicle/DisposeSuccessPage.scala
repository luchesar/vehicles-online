package helpers.disposal_of_vehicle

import pages.disposal_of_vehicle._

object DisposeSuccessPage {
  val url = "/disposal-of-vehicle/dispose-success"
  val title = "Dispose a vehicle into the motor trade: summary"

  def happyPath() {
    CacheSetup.setupTradeDetails()
    BusinessChooseYourAddressPage.setupCache()
    VehicleLookupPage.setupVehicleDetailsModelCache()
    DisposePage.setupDisposeFormModelCache
    DisposePage.setupDisposeTransactionIdCache
    DisposePage.setupRegistrationNumberCache()
  }

}
