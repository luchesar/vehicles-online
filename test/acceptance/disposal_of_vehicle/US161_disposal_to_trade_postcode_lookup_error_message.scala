package acceptance.disposal_of_vehicle

import org.scalatest.{BeforeAndAfterAll, Matchers, GivenWhenThen, FeatureSpec}
import pages.disposal_of_vehicle._
import helpers.webbrowser._
import helpers.disposal_of_vehicle.Helper._

class US161_disposal_to_trade_postcode_lookup_error_message extends FeatureSpec with GivenWhenThen with Matchers with BeforeAndAfterAll with TestHarness {

  feature("US161 Disposal to Trade - Postcode Lookup Error Message") {
    info("As a Motor Trader")
    info("I want to receive an appropriate error message when I enter a disallowed postcode")
    info("so that I can correct it and move to the next step in the transaction")
    info("")

    scenario("No postcode entered") {
      new WebBrowser {
        Given("the motor trader has entered no postcode")
        go to BeforeYouStartPage
        click on BeforeYouStartPage.startNow
        SetupTradeDetailsPage.dealerName enter traderBusinessNameValid
        // Note: leave the postcode field blank to cause errors.

        When("the postcode does not comply with formatting rules")
        click on SetupTradeDetailsPage.lookup

        Then("a single appropriate message is displayed")
        page.text should include("Must be between 5 and 8 characters and in the format PR2 8AE")

        And("the motor trader remains on the current step in the")
        page.title should equal("Provide your trader details")
      }
    }
  }

}
