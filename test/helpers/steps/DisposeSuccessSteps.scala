package helpers.steps

import cucumber.api.java.en.{Then, When, Given}
import helpers.webbrowser.{WebBrowserDSL, WebBrowserDriver}
import org.openqa.selenium.WebDriver
import org.scalatest.Matchers
import pages.disposal_of_vehicle._
import services.fakes.FakeVehicleLookupWebService._
import services.fakes.FakeAddressLookupService._
import services.fakes.FakeAddressLookupWebServiceImpl.traderUprnValid
import services.fakes.FakeDateServiceImpl._

final class DisposeSuccessSteps(webBrowserDriver:WebBrowserDriver) extends WebBrowserDSL with Matchers {

  implicit val webDriver = webBrowserDriver.asInstanceOf[WebDriver]

  @Given("""^that the user has completed the sale of a vehicle and received the transaction confirmation screen$""")
  def that_the_user_has_completed_the_sale_of_a_vehicle_and_received_the_transaction_confirmation_screen() = {
    go to BeforeYouStartPage
    click on BeforeYouStartPage.startNow
    SetupTradeDetailsPage.traderName enter TraderBusinessNameValid
    SetupTradeDetailsPage.traderPostcode enter PostcodeValid
    click on SetupTradeDetailsPage.lookup
    BusinessChooseYourAddressPage.chooseAddress.value = traderUprnValid.toString
    click on BusinessChooseYourAddressPage.select
    VehicleLookupPage.vehicleRegistrationNumber enter RegistrationNumberValid
    VehicleLookupPage.documentReferenceNumber enter ReferenceNumberValid
    click on VehicleLookupPage.findVehicleDetails
    DisposePage.mileage enter "10000"
    click on DisposePage.consent
    click on DisposePage.lossOfRegistrationConsent
    DisposePage.dateOfDisposalDay select DateOfDisposalDayValid
    DisposePage.dateOfDisposalMonth select DateOfDisposalMonthValid
    DisposePage.dateOfDisposalYear select DateOfDisposalYearValid
    click on DisposePage.dispose
    page.title should equal(DisposeSuccessPage.title)
  }

  @When("""^the user tries to return to a previous screen without using the "New Disposal" button$""")
  def the_user_tries_to_return_to_a_previous_screen_without_using_the_New_Disposal_button() = {
    webDriver.navigate().back()
  }

  @Then("""^the user is returned to the "Vehicle Lookup" page for the service$""")
  def the_user_is_returned_to_the_Vehicle_Lookup_page_for_the_service() = {
    page.title should equal(VehicleLookupPage.title)
  }

  @Then("""^the Trader details are retained$""")
  def the_Trader_details_are_retained() = {
    page.text should include(TraderBusinessNameValid.toUpperCase)
    page.text should include(PostcodeValid.toUpperCase)
  }

  @Then("""^no vehicle details are available$""")
  def no_vehicle_details_are_available() = {
    page.text should not include RegistrationNumberValid
    page.text should not include ReferenceNumberValid
  }

  @Given("""^that the user has selected the "Exit" button and navigated away from the service$""")
  def that_the_user_has_selected_the_Exit_button_and_navigated_away_from_the_service() = {
    go to BeforeYouStartPage
    click on BeforeYouStartPage.startNow
    SetupTradeDetailsPage.traderName enter TraderBusinessNameValid
    SetupTradeDetailsPage.traderPostcode enter PostcodeValid
    click on SetupTradeDetailsPage.lookup
    BusinessChooseYourAddressPage.chooseAddress.value = traderUprnValid.toString
    click on BusinessChooseYourAddressPage.select
    VehicleLookupPage.vehicleRegistrationNumber enter RegistrationNumberValid
    VehicleLookupPage.documentReferenceNumber enter ReferenceNumberValid
    click on VehicleLookupPage.findVehicleDetails
    DisposePage.mileage enter "10000"
    click on DisposePage.consent
    click on DisposePage.lossOfRegistrationConsent
    DisposePage.dateOfDisposalDay select DateOfDisposalDayValid
    DisposePage.dateOfDisposalMonth select DateOfDisposalMonthValid
    DisposePage.dateOfDisposalYear select DateOfDisposalYearValid
    click on DisposePage.dispose
    click on DisposeSuccessPage.exitDisposal
  }

  @When("""^the user tries to return to a previous screens in the service$""")
  def the_user_tries_to_return_to_a_previous_screens_in_the_service() = {
    webDriver.navigate().back()
  }

  @Then("""^the user is returned to the "SetUpTradeDetails" page for the service$""")
  def the_user_is_returned_to_the_Before_You_Start_page_for_the_service() = {
    page.title should equal(SetupTradeDetailsPage.title)
  }

  @Given("""^that the user has selected the "New Disposal" button and navigated away from the service$""")
  def that_the_user_has_selected_the_New_Disposal_button_and_navigated_away_from_the_service() = {
    go to BeforeYouStartPage
    click on BeforeYouStartPage.startNow
    SetupTradeDetailsPage.traderName enter TraderBusinessNameValid
    SetupTradeDetailsPage.traderPostcode enter PostcodeValid
    click on SetupTradeDetailsPage.lookup
    BusinessChooseYourAddressPage.chooseAddress.value = traderUprnValid.toString
    click on BusinessChooseYourAddressPage.select
    VehicleLookupPage.vehicleRegistrationNumber enter RegistrationNumberValid
    VehicleLookupPage.documentReferenceNumber enter ReferenceNumberValid
    click on VehicleLookupPage.findVehicleDetails
    DisposePage.mileage enter "10000"
    click on DisposePage.consent
    click on DisposePage.lossOfRegistrationConsent
    DisposePage.dateOfDisposalDay select DateOfDisposalDayValid
    DisposePage.dateOfDisposalMonth select DateOfDisposalMonthValid
    DisposePage.dateOfDisposalYear select DateOfDisposalYearValid
    click on DisposePage.dispose
    click on DisposeSuccessPage.newDisposal
  }
}
