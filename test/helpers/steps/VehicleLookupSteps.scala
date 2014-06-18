package helpers.steps

import pages.disposal_of_vehicle._
import cucumber.api.java.en.{Then, When, Given}
import org.scalatest.Matchers
import services.fakes.FakeVehicleLookupWebService._
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import org.openqa.selenium.WebDriver
import helpers.webbrowser.{WebBrowserDSL, WebBrowserDriver}
import mappings.disposal_of_vehicle.VehicleLookup._

final class VehicleLookupSteps(webBrowserDriver:WebBrowserDriver) extends WebBrowserDSL with Matchers {

  implicit val webDriver = webBrowserDriver.asInstanceOf[WebDriver]

  @Given("""^a correctly formatted document reference number "(.*)" has been entered$""")
  def a_correctly_formatted_document_reference_number_has_been_entered(docRefNo:String) = {
    buildVehicleLookupSetup()

    VehicleLookupPage.vehicleRegistrationNumber enter RegistrationNumberValid
    VehicleLookupPage.documentReferenceNumber enter docRefNo
  }
  @Given("""^an incorrectly formatted document reference number "(.*)" has been entered$""")
  def an_incorrectly_formatted_document_reference_number_has_been_entered(docRefNo:String) = {
    a_correctly_formatted_document_reference_number_has_been_entered(docRefNo)
  }
  @Given("""^a motor trader has entered a doc ref number in a valid format$""")
  def a_motor_trader_has_entered_a_doc_ref_number_in_a_valid_format() = {
    a_correctly_formatted_document_reference_number_has_been_entered(RegistrationNumberValid)
  }

  @Given("""^a motor trader has (.*) a VRM in a valid format$""")
  def a_motor_trader_has_entered_a_vrm_in_a_valid_format(vrm:String) = {
    buildVehicleLookupSetup()

    VehicleLookupPage.vehicleRegistrationNumber enter vrm
    VehicleLookupPage.documentReferenceNumber enter ReferenceNumberValid
  }

  @Given("""^a motor trader has (.*) a VRM in an invalid format$""")
  def a_motor_trader_has_entered_a_vrm_in_an_invalid_format(vrm:String) = {
    buildVehicleLookupSetup()

    VehicleLookupPage.vehicleRegistrationNumber enter vrm
    VehicleLookupPage.documentReferenceNumber enter ReferenceNumberValid
  }

  @Given("""^a motor trader has (.*) a doc ref number in an invalid format$""")
  def a_motor_trader_has_entered_a_doc_ref_number_in_an_invalid_format(invalidDocRef:String) = {
    buildVehicleLookupSetup()

    VehicleLookupPage.vehicleRegistrationNumber enter RegistrationNumberValid
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
    val vrn = webDriver.manage().
      getCookieNamed(VehicleLookupFormModelCacheKey).
      getValue

    vrn should include(ReferenceNumberValid)
  }

  @Then("""^the VRM is retained$""")
  def the_vrm_is_retained() = {
    page.text should include(RegistrationNumberValid)
  }

  private def buildVehicleLookupSetup() {
    go to BeforeYouStartPage

    CookieFactoryForUISpecs
      .setupTradeDetails()
      .dealerDetails()

    go to VehicleLookupPage
  }
}
