package helpers.steps

import pages.disposal_of_vehicle._
import cucumber.api.java.en.{Then, When, Given}
import org.scalatest.Matchers
import org.openqa.selenium.WebDriver
import helpers.webbrowser.{WebBrowserDSL, WebBrowserDriver}
import services.fakes.FakeAddressLookupService._

class TraderDetailsSteps(webBrowserDriver:WebBrowserDriver) extends WebBrowserDSL with Matchers {

  implicit val webDriver = webBrowserDriver.asInstanceOf[WebDriver]

  @Given("""^the motor trader has (.*) a business name which conforms to business rules$""")
  def the_motor_trader_has_entered_a_business_name_which_conforms_to_business_rules(traderName:String) = {
    checkTraderName(traderName)
  }

  @Given("""^the motor trader has (.*) a business name which does not conform to business rules$""")
  def the_motor_trader_has_entered_a_business_name_which_does_not_conform_to_business_rules(traderName:String) = {
    checkTraderName(traderName)
  }

  @When("""^they attempt to submit the business name in addition to other required information$""")
  def they_attempt_to_submit_the_business_name_in_addition_to_other_required_information() = {
    click on SetupTradeDetailsPage.lookup
  }

  @Then("""^the business name is retained$""")
  def the_business_name_is_retained() = {
  }

  private def checkTraderName(traderName: String) {
    go to SetupTradeDetailsPage
    SetupTradeDetailsPage.traderName enter traderName
    SetupTradeDetailsPage.traderPostcode enter postcodeValid
  }
}
