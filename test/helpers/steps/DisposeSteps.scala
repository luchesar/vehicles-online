package helpers.steps

import pages.disposal_of_vehicle._
import cucumber.api.java.en.{When, Given}
import org.scalatest.Matchers
import services.fakes.FakeDateServiceImpl._
import helpers.disposal_of_vehicle.CacheSetup
import org.openqa.selenium.WebDriver
import helpers.webbrowser.{WebBrowserDSL, WebBrowserDriver}
import services.session.PlaySessionState
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState

class DisposeSteps(webBrowserDriver:WebBrowserDriver) extends WebBrowserDSL with Matchers {

  implicit val webDriver = webBrowserDriver.asInstanceOf[WebDriver]

  val sessionState = new DisposalOfVehicleSessionState(new PlaySessionState())
  // TODO [SKW] code re-use please - there is a LOT of duplication in this class.
  @Given("""^the motor trader has confirmed the consent of the current keeper$""")
  def the_motor_trader_has_confirmed_the_consent_of_the_current_keeper() = {
    go to BeforeYouStartPage
    new CacheSetup(sessionState.inner)
      .setupTradeDetailsIntegration()
      .dealerDetailsIntegration()
      .vehicleDetailsModelIntegration()
      .vehicleLookupFormModelIntegration()

    go to DisposePage
    DisposePage.dateOfDisposalDay select dateOfDisposalDayValid
    DisposePage.dateOfDisposalMonth select dateOfDisposalMonthValid
    DisposePage.dateOfDisposalYear select dateOfDisposalYearValid
    click on DisposePage.consent
    click on DisposePage.lossOfRegistrationConsent
  }

  @Given("""^the motor trader has not confirmed the consent of the current keeper$""")
  def the_motor_trader_has_not_confirmed_the_consent_of_the_current_keeper() = {
    go to BeforeYouStartPage
    new CacheSetup(sessionState.inner)
      .setupTradeDetailsIntegration()
      .dealerDetailsIntegration()
      .vehicleDetailsModelIntegration()
      .vehicleLookupFormModelIntegration()

    go to DisposePage
    DisposePage.dateOfDisposalDay select dateOfDisposalDayValid
    DisposePage.dateOfDisposalMonth select dateOfDisposalMonthValid
    DisposePage.dateOfDisposalYear select dateOfDisposalYearValid
    click on DisposePage.lossOfRegistrationConsent
  }

  @Given("""^the motor trader has confirmed the acknowledgement of the current keeper$""")
  def the_motor_trader_has_confirmed_the_acknowledgement_of_the_current_keeper() = {
    go to BeforeYouStartPage
    new CacheSetup(sessionState.inner)
      .setupTradeDetailsIntegration()
      .dealerDetailsIntegration()
      .vehicleDetailsModelIntegration()
      .vehicleLookupFormModelIntegration()

    go to DisposePage
    DisposePage.dateOfDisposalDay select dateOfDisposalDayValid
    DisposePage.dateOfDisposalMonth select dateOfDisposalMonthValid
    DisposePage.dateOfDisposalYear select dateOfDisposalYearValid
    click on DisposePage.consent
    click on DisposePage.lossOfRegistrationConsent
  }

  @Given("""^the motor trader has not confirmed the acknowledgement of the current keeper$""")
  def the_motor_trader_has_not_confirmed_the_acknowledgement_of_the_current_keeper() = {
    go to BeforeYouStartPage
    new CacheSetup(sessionState.inner)
      .setupTradeDetailsIntegration()
      .dealerDetailsIntegration()
      .vehicleDetailsModelIntegration()
      .vehicleLookupFormModelIntegration()

    go to DisposePage
    DisposePage.dateOfDisposalDay select dateOfDisposalDayValid
    DisposePage.dateOfDisposalMonth select dateOfDisposalMonthValid
    DisposePage.dateOfDisposalYear select dateOfDisposalYearValid
    click on DisposePage.consent
  }

  @Given("""^the motor trader has entered a valid calendar date which conforms to business rules$""")
  def the_motor_trader_has_entered_a_valid_calendar_date() = {
    go to BeforeYouStartPage
    new CacheSetup(sessionState.inner)
      .setupTradeDetailsIntegration()
      .dealerDetailsIntegration()
      .vehicleDetailsModelIntegration()
      .vehicleLookupFormModelIntegration()

    go to DisposePage
    DisposePage.dateOfDisposalDay select dateOfDisposalDayValid
    DisposePage.dateOfDisposalMonth select dateOfDisposalMonthValid
    DisposePage.dateOfDisposalYear select dateOfDisposalYearValid
    click on DisposePage.consent
    click on DisposePage.lossOfRegistrationConsent
  }

  @Given("""^the motor trader has entered a valid calendar date which does not conform to business rules$""")
  def the_motor_trader_has_entered_a_valid_calendar_date_which_does_not_conform_to_the_business_rules() = {
    go to BeforeYouStartPage
    new CacheSetup(sessionState.inner)
      .setupTradeDetailsIntegration()
      .dealerDetailsIntegration()
      .vehicleDetailsModelIntegration()
      .vehicleLookupFormModelIntegration()

    go to DisposePage
    // Leave not filled in for npw - need way to convert select elements to text fields ala Web Developer toolbar, via proxy perhaps?
    //DisposePage.dateOfDisposalDay select dateOfDisposalDayValid
    //DisposePage.dateOfDisposalMonth select dateOfDisposalMonthValid
    //DisposePage.dateOfDisposalYear select dateOfDisposalYearValid
    click on DisposePage.consent
    click on DisposePage.lossOfRegistrationConsent
  }

  @Given("""^that entered details correspond to a valid clean record that has no markers or error codes$""")
  def that_entered_details_correspond_to_a_valid_clean_record_that_has_no_markers_or_error_codes() = {
    go to BeforeYouStartPage
    new CacheSetup(sessionState.inner)
      .setupTradeDetailsIntegration()
      .dealerDetailsIntegration()

    go to VehicleLookupPage
    VehicleLookupPage.vehicleRegistrationNumber enter "AB12AWR"
    VehicleLookupPage.documentReferenceNumber enter "11111111112"
    click on VehicleLookupPage.findVehicleDetails
    DisposePage.dateOfDisposalDay select dateOfDisposalDayValid
    DisposePage.dateOfDisposalMonth select dateOfDisposalMonthValid
    DisposePage.dateOfDisposalYear select dateOfDisposalYearValid
    click on DisposePage.consent
    click on DisposePage.lossOfRegistrationConsent
  }

  @Given("""^that entered details correspond to a valid record which has markers or error codes$""")
  def that_entered_details_correspond_to_a_valid_record_which_has_markers_or_error_codes() = {
    go to BeforeYouStartPage
    new CacheSetup(sessionState.inner)
      .setupTradeDetailsIntegration()
      .dealerDetailsIntegration()

    go to VehicleLookupPage
    VehicleLookupPage.vehicleRegistrationNumber enter "AB12AWR"
    VehicleLookupPage.documentReferenceNumber enter "11111111113"
    click on VehicleLookupPage.findVehicleDetails
    DisposePage.dateOfDisposalDay select dateOfDisposalDayValid
    DisposePage.dateOfDisposalMonth select dateOfDisposalMonthValid
    DisposePage.dateOfDisposalYear select dateOfDisposalYearValid
    click on DisposePage.consent
    click on DisposePage.lossOfRegistrationConsent
  }

  @When("""^they attempt to dispose of the vehicle$""")
  def they_attempt_to_dispose_of_the_vehicle() = {
    click on DisposePage.dispose
  }
}
