package functional

import org.scalatest.{BeforeAndAfterAll, Matchers, GivenWhenThen, FeatureSpec}
import pages.disposal_of_vehicle._
import helpers.webbrowser._

class SetUpTradeDetailsSpec extends FeatureSpec with GivenWhenThen with Matchers with BeforeAndAfterAll with TestHarness {

  feature("Dispose of a vehicle to trade") {
      info("As a Motor Trader")
      info("I want to receive an appropriate error message when I enter a disallowed Business Name")
      info("so that I can move to the next step in the transaction")
      info("")

      scenario("US158: Disposal to Trade: Business Name - Error Messages") {

        new WebBrowser {
          Given("the motor trader has entered a Business Name")
          go to BeforeYouStartPage
          click on BeforeYouStartPage.startNow
          // Note: leave the dealerName field blank to cause errors.
          SetupTradeDetailsPage.dealerPostcode enter "CM8 1QJ"

          When("the Business Name does not comply with formatting rules")
          click on SetupTradeDetailsPage.lookup

          Then("a single appropriate message is displayed")
          page.text should include("Must be between two and 30 characters and not contain invalid characters")

          And("the motor trader remains on the current step in the")
          page.title should equal("Provide your trader details")
        }
      }
    }
}
