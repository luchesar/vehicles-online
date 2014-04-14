package acceptance.disposal_of_vehicle

import org.scalatest.{BeforeAndAfterAll, Matchers, GivenWhenThen, FeatureSpec}
import pages.disposal_of_vehicle._
import helpers.webbrowser._
import helpers.disposal_of_vehicle.CacheSetup
import services.fakes.FakeVehicleLookupWebService._
import VehicleLookupPage._

class US43_validate_document_reference_entry_format_error_messages extends FeatureSpec with GivenWhenThen with Matchers with BeforeAndAfterAll with TestHarness {

  private def cacheSetup() = {
    CacheSetup.setupTradeDetails().
      businessChooseYourAddress()
  }

  feature("US43: Validate document reference entry format - Error Messages") {
    info("As a Motor Trader")
    info("I want to receive an appropriate error message when I enter an invalid document reference number")
    info("so that I can correct it and move to the next step in the transaction")
    info("")

    scenario("No document reference number entered") {
      new WebBrowser {
        Given("the motor trader has entered a doc ref number in an invalid format (non-digits, empty and/or insufficient length)")
        cacheSetup()
        go to VehicleLookupPage

        // Note: leave the document reference number field blank to cause errors.
        vehicleRegistrationNumber enter registrationNumberValid

        When("they attempt to submit the information")
        click on findVehicleDetails

        Then("a single appropriate message is displayed")
        page.text should include("Must be an 11-digit number")

        And("the motor trader remains on the current step")
        page.title should equal("Find vehicle details")
      }
    }

    scenario("Valid data entered") {
      new WebBrowser {
        Given("the motor trader has entered valid data")
        cacheSetup()
        go to VehicleLookupPage

        // Populate everything correctly
        vehicleRegistrationNumber enter registrationNumberValid
        documentReferenceNumber enter referenceNumberValid

        When("the data complies with formatting rules")
        click on findVehicleDetails

        And("the motor trader progresses to the next step in the transaction")
        page.title should equal("Complete & confirm")
      }
    }
  }
}
