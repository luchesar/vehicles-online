package acceptance.disposal_of_vehicle

import org.scalatest.{BeforeAndAfterAll, Matchers, GivenWhenThen, FeatureSpec}
import helpers.webbrowser._
import helpers.disposal_of_vehicle.CacheSetup
import pages.disposal_of_vehicle.DisposePage
import pages.disposal_of_vehicle.DisposePage._
import services.fakes.FakeDateServiceImpl._

class US174_disposal_to_trade_disposal_submission_ms_to_vss_failure extends FeatureSpec with GivenWhenThen with Matchers with BeforeAndAfterAll with TestHarness {
  private def cacheSetup() = {
    CacheSetup.setupTradeDetails()
    CacheSetup.businessChooseYourAddress()
    CacheSetup.vehicleDetailsModel()
    CacheSetup.vehicleLookupFormModel(referenceNumber = "9" * 11)
  }

  feature("US158: Disposal to Trade: Business Name - Error Messages") {
    info("As a Motor Trader")
    info("I want to receive an appropriate error message when I enter a disallowed Business Name")
    info("so that I can move to the next step in the transaction")
    info("")

    scenario("No business name entered") {
      new WebBrowser {
        Given("the motor trader has tried to submit the disposal")
        And("VSS is unavailable")
        cacheSetup()
        go to DisposePage
        dateOfDisposalDay select dateOfDisposalDayValid
        dateOfDisposalMonth select dateOfDisposalMonthValid
        dateOfDisposalYear select dateOfDisposalYearValid
        click on DisposePage.consent
        click on DisposePage.lossOfRegistrationConsent

        When("the Business Name does not comply with formatting rules")
        click on dispose

        Then("an error message is displayed")
        page.title should equal("We are sorry")

        And("they are given the option to either go back to the submission page or exit the service")
        page.text should include("Back")
        page.text should include("Exit")
      }
    }
  }
}
