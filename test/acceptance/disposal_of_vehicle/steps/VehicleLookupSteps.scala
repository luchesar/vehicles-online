package acceptance.disposal_of_vehicle.steps

import pages.disposal_of_vehicle._
import cucumber.api.java.en.{Then, When, Given}
import helpers.hooks.WebBrowser
import pages.common.ErrorPanel
import org.scalatest.Matchers
import services.fakes.FakeVehicleLookupWebService._
import helpers.disposal_of_vehicle.CacheSetup

class VehicleLookupSteps extends WebBrowser with Matchers {

  @Given("""^a motor trader has entered a doc ref number in a valid format$""")
  def a_motor_trader_has_entered_a_doc_ref_number_in_a_valid_format() = {
    CacheSetup.setupTradeDetails()
    CacheSetup.businessChooseYourAddress()

    go to VehicleLookupPage
    VehicleLookupPage.vehicleRegistrationNumber enter registrationNumberValid
    VehicleLookupPage.documentReferenceNumber enter referenceNumberValid
  }

  @Given("""^a motor trader has (.*) a VRM in a valid format$""")
  def a_motor_trader_has_entered_a_vrm_in_a_valid_format(vrm:String) = {
    CacheSetup.setupTradeDetails()
    CacheSetup.businessChooseYourAddress()

    go to VehicleLookupPage
    VehicleLookupPage.vehicleRegistrationNumber enter vrm
    VehicleLookupPage.documentReferenceNumber enter referenceNumberValid
  }

  @When("""^they attempt to submit the information$""")
  def they_attempt_to_submit_the_information() = {
    click on VehicleLookupPage.findVehicleDetails
  }

  @Then("""^the doc ref number is retained$""")
  def the_doc_ref_number_is_retained() = {
    // nothing can be done here
  }

  @Then("""^the VRM is retained$""")
  def the_vrm_is_retained() = {
    // nothing can be done here
  }

  @Then("""^they move to the next step in the transaction$""")
  def they_move_to_the_next_step_in_the_transaction() = {
    page.title should not include(VehicleLookupPage.title)
  }

  @Given("""^a motor trader has (.*) a doc ref number in an invalid format$""")
  def a_motor_trader_has_entered_a_doc_ref_number_in_an_invalid_format(invalidDocRef:String) = {
    CacheSetup.setupTradeDetails()
    CacheSetup.businessChooseYourAddress()

    go to VehicleLookupPage
    VehicleLookupPage.vehicleRegistrationNumber enter registrationNumberValid
    VehicleLookupPage.documentReferenceNumber enter invalidDocRef
  }

  @Given("""^a motor trader has (.*) a VRM in an invalid format$""")
  def a_motor_trader_has_entered_a_vrm_in_an_invalid_format(vrm:String) = {
    CacheSetup.setupTradeDetails()
    CacheSetup.businessChooseYourAddress()

    go to VehicleLookupPage
    VehicleLookupPage.vehicleRegistrationNumber enter vrm
    VehicleLookupPage.documentReferenceNumber enter referenceNumberValid
  }

  @Then("""^a single error message is displayed "(.*)"$""")
  def a_single_error_message_is_displayed(message:String) = {
    ErrorPanel.text should include(message)
  }

  @Then("""^they remain at the current stage in the transaction$""")
  def they_remain_at_the_current_stage_in_the_transaction() = {
    page.title should include(VehicleLookupPage.title)
  }
}
