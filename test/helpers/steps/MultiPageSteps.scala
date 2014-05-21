package helpers.steps

import cucumber.api.java.en.{Given, When, Then}
import pages.disposal_of_vehicle._
import org.scalatest.Matchers
import org.openqa.selenium.WebDriver
import helpers.webbrowser.{WebBrowserDSL, WebBrowserDriver}
import helpers.disposal_of_vehicle.Helper._
import services.fakes.FakeDateServiceImpl._
import services.fakes.FakeVehicleLookupWebService._
import services.fakes.FakeAddressLookupService._
import services.fakes.FakeWebServiceImpl.traderUprnValid
import mappings.disposal_of_vehicle.Dispose._

final class MultiPageSteps(webBrowserDriver:WebBrowserDriver) extends WebBrowserDSL with Matchers {

  implicit val webDriver = webBrowserDriver.asInstanceOf[WebDriver]

  @Given("""^that the user has entered all required information$""")
  def that_the_user_has_entered_all_required_information() = {
    go to BeforeYouStartPage
    click on BeforeYouStartPage.startNow
    SetupTradeDetailsPage.traderName enter traderBusinessNameValid
    SetupTradeDetailsPage.traderPostcode enter postcodeValid
    click on SetupTradeDetailsPage.lookup
    BusinessChooseYourAddressPage.chooseAddress.value = traderUprnValid.toString
    click on BusinessChooseYourAddressPage.select
    VehicleLookupPage.vehicleRegistrationNumber enter registrationNumberValid
    VehicleLookupPage.documentReferenceNumber enter referenceNumberValid
    click on VehicleLookupPage.findVehicleDetails
  }

  @Given("""^that a trader has successfully completed a disposal submission$""")
  def that_a_trader_has_successfully_completed_a_disposal_submission() = {
    that_the_user_has_entered_all_required_information()
    DisposePage.mileage enter "10000"
    click on DisposePage.consent
    click on DisposePage.lossOfRegistrationConsent
    DisposePage.dateOfDisposalDay select dateOfDisposalDayValid
    DisposePage.dateOfDisposalMonth select dateOfDisposalMonthValid
    DisposePage.dateOfDisposalYear select dateOfDisposalYearValid
    click on DisposePage.dispose
    page.title should equal(DisposeSuccessPage.title)
  }

  @When("""^the summary page has been reached$""")
  def the_summary_page_has_been_reached() = {
    page.title should equal(DisposePage.title)
  }

  @When("""^they wish to start a new disposal transaction$""")
  def they_wish_to_start_a_new_disposal_transaction() = {
    click on DisposeSuccessPage.newDisposal
  }

  @Then("""^the trader name and address details are pre populated$""")
  def the_trader_name_and_address_details_are_pre_populated() = {
    page.title should equal(VehicleLookupPage.title)
    page.text should include(traderBusinessNameValid)
    enterValidManualAddress

  }

  @Then("""^the information entered is presented back to the motor trader$""")
  def the_information_entered_is_presented_back_to_the_motor_trader() = {
    page.text should include(traderBusinessNameValid)
    page.text should include(registrationNumberValid)
    enterValidManualAddress
    DisposePage.consent.isDisplayed should equal(true)
    DisposePage.lossOfRegistrationConsent.isDisplayed should equal(true)
    DisposePage.mileage.isDisplayed should equal(true)
    DisposePage.dateOfDisposalDay.isDisplayed should equal(true)
    DisposePage.dateOfDisposalMonth.isDisplayed should equal(true)
    DisposePage.dateOfDisposalYear.isDisplayed should equal(true)
  }

  @Then("""^a timestamp representing the current date and time is generated and retained$""")
  def a_timestamp_representing_the_current_date_and_time_is_generated_and_retained() = {
    val timestamp = webDriver.manage().
      getCookieNamed(DisposeFormTimestampIdCacheKey).
      getValue

    timestamp should include(s"""$dateOfDisposalYearValid-$dateOfDisposalMonthValid-${dateOfDisposalDayValid}""")
  }

  private def enterValidManualAddress() {
    page.text should include("presentationProperty stub")
    page.text should include("property stub")
    page.text should include("street stub")
    page.text should include("town stub")
    page.text should include("area stub")
    page.text should include(postcodeValid)
  }
}
