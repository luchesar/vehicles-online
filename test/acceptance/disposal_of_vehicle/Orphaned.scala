package acceptance.disposal_of_vehicle

import org.scalatest.{BeforeAndAfterAll, Matchers, GivenWhenThen, FeatureSpec}
import helpers.webbrowser._
import helpers.disposal_of_vehicle.Helper._
import helpers.disposal_of_vehicle.CacheSetup
import models.domain.disposal_of_vehicle.AddressViewModel
import pages.disposal_of_vehicle.VehicleLookupPage

class Orphaned extends FeatureSpec with GivenWhenThen with Matchers with BeforeAndAfterAll with TestHarness {

  private def cacheSetup() = {
    CacheSetup.setupTradeDetails("CT117PB")
    CacheSetup.businessChooseYourAddress(new AddressViewModel(address = Seq("My address", "London")))
  }

  feature("US43: Validate vehicle registration number format - Error Messages") {
    info("As a Motor Trader")
    info("I want to receive an appropriate error message when I enter an invalid vehicle registration number")
    info("so that I can correct it and move to the next step in the transaction")
    info("")

    scenario("No vehicle registration number entered") {
      new WebBrowser {
        Given("the motor trader has entered a vehicle registration number in an invalid format")
        cacheSetup
        go to VehicleLookupPage

        // Note: leave the vehicle registration number field blank to cause errors.
        VehicleLookupPage.documentReferenceNumber enter referenceNumberValid
        click on VehicleLookupPage.consent

        When("they attempt to submit the information")
        click on VehicleLookupPage.findVehicleDetails

        Then("a single appropriate message is displayed")
        page.text should include("Please enter a valid vehicle registration number")

        And("the motor trader remains on the current step")
        page.title should equal("Find vehicle details")
      }
    }
  }
}
