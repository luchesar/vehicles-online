package helpers.steps

import pages.disposal_of_vehicle._
import cucumber.api.java.en.{Then, When, Given}
import org.scalatest.Matchers
import services.fakes.FakeVehicleLookupWebService._
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import org.openqa.selenium.WebDriver
import helpers.webbrowser.{WebBrowserDSL, WebBrowserDriver}

class VehicleLookupSteps(webBrowserDriver:WebBrowserDriver) extends WebBrowserDSL with Matchers {

  implicit val webDriver = webBrowserDriver.asInstanceOf[WebDriver]

  @Given("""^a motor trader has entered a doc ref number in a valid format$""")
  def a_motor_trader_has_entered_a_doc_ref_number_in_a_valid_format() = {
    go to BeforeYouStartPage
    new CookieFactoryForUISpecs()
      .setupTradeDetailsIntegration()
      .dealerDetailsIntegration()

    go to VehicleLookupPage
    VehicleLookupPage.vehicleRegistrationNumber enter registrationNumberValid
    VehicleLookupPage.documentReferenceNumber enter referenceNumberValid
  }

  @Given("""^a motor trader has (.*) a VRM in a valid format$""")
  def a_motor_trader_has_entered_a_vrm_in_a_valid_format(vrm:String) = {
    go to BeforeYouStartPage
    new CookieFactoryForUISpecs()
      .setupTradeDetailsIntegration()
      .dealerDetailsIntegration()

    go to VehicleLookupPage
    VehicleLookupPage.vehicleRegistrationNumber enter vrm
    VehicleLookupPage.documentReferenceNumber enter referenceNumberValid
  }

  @Given("""^a motor trader has (.*) a VRM in an invalid format$""")
  def a_motor_trader_has_entered_a_vrm_in_an_invalid_format(vrm:String) = {
    go to BeforeYouStartPage
    new CookieFactoryForUISpecs()
      .setupTradeDetailsIntegration()
      .dealerDetailsIntegration()

    go to VehicleLookupPage
    VehicleLookupPage.vehicleRegistrationNumber enter vrm
    VehicleLookupPage.documentReferenceNumber enter referenceNumberValid
  }

  @Given("""^a motor trader has (.*) a doc ref number in an invalid format$""")
  def a_motor_trader_has_entered_a_doc_ref_number_in_an_invalid_format(invalidDocRef:String) = {
    go to BeforeYouStartPage
    new CookieFactoryForUISpecs()
      .setupTradeDetailsIntegration()
      .dealerDetailsIntegration()

    go to VehicleLookupPage
    VehicleLookupPage.vehicleRegistrationNumber enter registrationNumberValid
    VehicleLookupPage.documentReferenceNumber enter invalidDocRef
  }

  @When("""^they attempt to submit the VRM in addition to other required information$""")
  def they_attempt_to_submit_the_VRM_in_addition_to_other_required_information() = {
    click on VehicleLookupPage.findVehicleDetails
  }

  @When("""^they attempt to submit the doc ref number in addition to other required information$""")
  def they_attempt_to_submit_the_doc_ref_number_in_addition_to_other_required_information() = {
    click on VehicleLookupPage.findVehicleDetails
  }

  @Then("""^the doc ref number is retained$""")
  def the_doc_ref_number_is_retained() = {
    // nothing can be done here to check for this as doc ref no is not displayed
    // TODO NO!!! CHECK if it is in the cache
  }

  @Then("""^the VRM is retained$""")
  def the_vrm_is_retained() = {
    page.text should include(registrationNumberValid)
  }
}
