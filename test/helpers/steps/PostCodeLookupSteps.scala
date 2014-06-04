package helpers.steps

import pages.disposal_of_vehicle._
import cucumber.api.java.en.{Then, When, Given}
import org.scalatest.Matchers
import org.openqa.selenium.WebDriver
import helpers.webbrowser.{WebBrowserDSL, WebBrowserDriver}
import services.fakes.FakeAddressLookupService.TraderBusinessNameValid

final class PostCodeLookupSteps(webBrowserDriver:WebBrowserDriver) extends WebBrowserDSL with Matchers {

  implicit val webDriver = webBrowserDriver.asInstanceOf[WebDriver]

  @Given("""^the motor trader has (.*) a postcode which conforms to business rules$""")
  def the_motor_trader_has_entered_a_postcode_which_conforms_to_business_rules(testedPostcode:String) = {
    checkTraderPostcode(testedPostcode)
  }

  @Given("""^the motor trader has (.*) a postcode which does not conform to business rules$""")
  def the_motor_trader_has_entered_a_postcode_which_does_not_conform_to_business_rules(testedPostcode:String) = {
    checkTraderPostcode(testedPostcode)
  }

  @When("""^they attempt to submit the postcode in addition to other required information$""")
  def they_attempt_to_submit_the_postcode_in_addition_to_other_required_information() = {
    click on SetupTradeDetailsPage.lookup
  }

  @Then("""^the postcode is retained$""")
  def the_postcode_is_retained() = {
    //
  }

  private def checkTraderPostcode(testedPostcode: String) {
    go to SetupTradeDetailsPage
    SetupTradeDetailsPage.traderName enter TraderBusinessNameValid
    SetupTradeDetailsPage.traderPostcode enter testedPostcode
  }
}
