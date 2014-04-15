package acceptance.disposal_of_vehicle.steps

import pages.disposal_of_vehicle._
import cucumber.api.java.en.{When, Given}
import org.scalatest.Matchers
import services.fakes.FakeDateServiceImpl._
import helpers.disposal_of_vehicle.CacheSetup
import org.openqa.selenium.WebDriver
import helpers.webbrowser.{WebBrowserDSL, WebBrowserDriver}

class DisposeSteps(webBrowserDriver:WebBrowserDriver) extends WebBrowserDSL with Matchers {

  implicit val webDriver = webBrowserDriver.asInstanceOf[WebDriver]

  @Given("""^the motor trader has confirmed the consent of the current keeper$""")
  def the_motor_trader_has_confirmed_the_consent_of_the_current_keeper() = {
    CacheSetup.setupTradeDetails()
    CacheSetup.businessChooseYourAddress()
    CacheSetup.vehicleDetailsModel()
    CacheSetup.vehicleLookupFormModel()

    go to DisposePage
    DisposePage.dateOfDisposalDay select dateOfDisposalDayValid
    DisposePage.dateOfDisposalMonth select dateOfDisposalMonthValid
    DisposePage.dateOfDisposalYear select dateOfDisposalYearValid
    click on DisposePage.consent
    click on DisposePage.lossOfRegistrationConsent
  }

  @Given("""^the motor trader has not confirmed the consent of the current keeper$""")
  def the_motor_trader_has_not_confirmed_the_consent_of_the_current_keeper() = {
    CacheSetup.setupTradeDetails()
    CacheSetup.businessChooseYourAddress()
    CacheSetup.vehicleDetailsModel()
    CacheSetup.vehicleLookupFormModel()

    go to DisposePage
    DisposePage.dateOfDisposalDay select dateOfDisposalDayValid
    DisposePage.dateOfDisposalMonth select dateOfDisposalMonthValid
    DisposePage.dateOfDisposalYear select dateOfDisposalYearValid
    click on DisposePage.lossOfRegistrationConsent
  }

  @Given("""^the motor trader has confirmed the acknowledgement of the current keeper$""")
  def the_motor_trader_has_confirmed_the_acknowledgement_of_the_current_keeper() = {
    CacheSetup.setupTradeDetails()
    CacheSetup.businessChooseYourAddress()
    CacheSetup.vehicleDetailsModel()
    CacheSetup.vehicleLookupFormModel()

    go to DisposePage
    DisposePage.dateOfDisposalDay select dateOfDisposalDayValid
    DisposePage.dateOfDisposalMonth select dateOfDisposalMonthValid
    DisposePage.dateOfDisposalYear select dateOfDisposalYearValid
    click on DisposePage.consent
    click on DisposePage.lossOfRegistrationConsent
  }

  @Given("""^the motor trader has not confirmed the acknowledgement of the current keeper$""")
  def the_motor_trader_has_not_confirmed_the_acknowledgement_of_the_current_keeper() = {
    CacheSetup.setupTradeDetails()
    CacheSetup.businessChooseYourAddress()
    CacheSetup.vehicleDetailsModel()
    CacheSetup.vehicleLookupFormModel()

    go to DisposePage
    DisposePage.dateOfDisposalDay select dateOfDisposalDayValid
    DisposePage.dateOfDisposalMonth select dateOfDisposalMonthValid
    DisposePage.dateOfDisposalYear select dateOfDisposalYearValid
    click on DisposePage.consent
  }

  @Given("""^the motor trader has entered a valid calendar date which conforms to business rules$""")
  def the_motor_trader_has_entered_a_valid_calendar_date() = {
    CacheSetup.setupTradeDetails()
    CacheSetup.businessChooseYourAddress()
    CacheSetup.vehicleDetailsModel()
    CacheSetup.vehicleLookupFormModel()

    go to DisposePage
    DisposePage.dateOfDisposalDay select dateOfDisposalDayValid
    DisposePage.dateOfDisposalMonth select dateOfDisposalMonthValid
    DisposePage.dateOfDisposalYear select dateOfDisposalYearValid
    click on DisposePage.consent
    click on DisposePage.lossOfRegistrationConsent
  }

  @Given("""^the motor trader has entered a valid calendar date which does not conform to business rules$""")
  def the_motor_trader_has_entered_a_valid_calendar_date_which_does_not_conform_to_the_business_rules() = {
    CacheSetup.setupTradeDetails()
    CacheSetup.businessChooseYourAddress()
    CacheSetup.vehicleDetailsModel()
    CacheSetup.vehicleLookupFormModel()

    go to DisposePage
    // Leave not filled in for npw - need way to convert select elements to text fields ala Web Developer toolbar, via proxy perhaps?
    //DisposePage.dateOfDisposalDay select dateOfDisposalDayValid
    //DisposePage.dateOfDisposalMonth select dateOfDisposalMonthValid
    //DisposePage.dateOfDisposalYear select dateOfDisposalYearValid
    click on DisposePage.consent
    click on DisposePage.lossOfRegistrationConsent
    DisposeSuccessPage
  }

  @When("""^they attempt to submit the consent in addition to other required information$""")
  def they_attempt_to_submit_the_consent_in_addition_to_other_required_information() = {
    click on DisposePage.dispose
  }

  @When("""^they attempt to submit the acknowledgement in addition to other required information$""")
  def they_attempt_to_submit_the_acknowledgement_in_addition_to_other_required_information() = {
    click on DisposePage.dispose
  }

  @When("""^they attempt to submit the date in addition to other required information$""")
  def they_attempt_to_submit_the_date_in_addition_to_other_required_information() = {
    click on DisposePage.dispose
  }
}
