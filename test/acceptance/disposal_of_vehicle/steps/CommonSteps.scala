package acceptance.disposal_of_vehicle.steps

import cucumber.api.java.en.{Given, When, Then}
import pages.common.ErrorPanel
import pages.disposal_of_vehicle._
import org.scalatest.Matchers
import org.openqa.selenium.WebDriver
import helpers.webbrowser.{WebBrowserDSL, WebBrowserDriver}
import helpers.disposal_of_vehicle.Helper._
import services.fakes.FakeDateServiceImpl._
import services.fakes.FakeVehicleLookupWebService._
import services.fakes.FakeAddressLookupService._
import services.fakes.FakeDisposeWebServiceImpl._
import services.fakes.FakeWebServiceImpl.traderUprnValid

class CommonSteps(webBrowserDriver:WebBrowserDriver) extends WebBrowserDSL with Matchers {

  implicit val webDriver = webBrowserDriver.asInstanceOf[WebDriver]

  @Given("""^that the user has entered all required information$""")
  def that_the_user_has_entered_all_required_information() = {
    go to BeforeYouStartPage
    click on BeforeYouStartPage.startNow
    SetupTradeDetailsPage.dealerName enter traderBusinessNameValid
    SetupTradeDetailsPage.dealerPostcode enter postcodeValid
    click on SetupTradeDetailsPage.lookup
    BusinessChooseYourAddressPage.chooseAddress.value = traderUprnValid.toString
    click on BusinessChooseYourAddressPage.select
    VehicleLookupPage.vehicleRegistrationNumber enter registrationNumberValid
    VehicleLookupPage.documentReferenceNumber enter referenceNumberValid
    click on VehicleLookupPage.findVehicleDetails
  }

  @When("""^the summary page has been reached$""")
  def the_summary_page_has_been_reached() = {
    page.title should equal(DisposePage.title)
  }

  @Then("""^the information entered is presented back to the motor trader$""")
  def the_information_entered_is_presented_back_to_the_motor_trader() = {
    page.text should include(traderBusinessNameValid)
    page.text should include(registrationNumberValid)
    page.text should include("presentationProperty stub")
    page.text should include("property stub")
    page.text should include("street stub")
    page.text should include("town stub")
    page.text should include("area stub")
    page.text should include(postcodeValid)
    DisposePage.consent.isDisplayed should equal(true)
    DisposePage.lossOfRegistrationConsent.isDisplayed should equal(true)
    DisposePage.mileage.isDisplayed should equal(true)
  }

  @Then("""^a single error message is displayed "(.*)"$""")
  def a_single_error_message_is_displayed(message:String) = {
    ErrorPanel.numberOfErrors should equal(1)
    ErrorPanel.text should include(message)
  }

  @Then("""^they are taken to the "(.*)" page$""")
  def they_are_taken_to_the_next_step_in_the_transaction(title:String) = {
    page.title should equal(title)
  }

  @Then("""^they remain on the "(.*)" page$""")
  def they_remain_at_the_current_stage_in_the_transaction(title:String) = {
    page.title should equal(title)
  }
}
