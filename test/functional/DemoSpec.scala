package functional

import org.scalatest.{BeforeAndAfterAll, Matchers, GivenWhenThen, FeatureSpec}
import pages.disposal_of_vehicle._
import helpers.webbrowser._
import services.fakes.FakeWebServiceImpl.uprnValid
import helpers.disposal_of_vehicle.Helper._
import services.fakes.FakeDateServiceImpl._
import services.fakes.FakeDisposeWebServiceImpl._
import services.fakes.FakeVehicleLookupWebService._

class DemoSpec extends FeatureSpec with GivenWhenThen with Matchers with BeforeAndAfterAll with TestHarness {

  feature("Dispose of a vehicle to trade") {

      info("As a vehicle trader")
      info("I want to dispose of a vehicle for a customer")
      info("So they will be removed from the vehicle record as current keeper")
      info("")

      scenario("Dispose a vehicle to the trade: happy path") {

        new WebBrowser {

          Given("I am on the vehicles online prototype site")
          go to BeforeYouStartPage

          And("I click the Start now button to begin the transaction")
          click on BeforeYouStartPage.startNow

          And("I enter \"Car Giant\" in the business name field")
          SetupTradeDetailsPage.dealerName enter traderBusinessNameValid

          And("I enter \"CM8 1QJ\" in the business postcode field")
          SetupTradeDetailsPage.dealerPostcode enter postcodeValidWithSpace

          And("I click the Look-up button")
          click on SetupTradeDetailsPage.lookup

          And("I select \"1, OLIVERS DRIVE, WITHAM, CM8 1QJ\"")
          BusinessChooseYourAddressPage.chooseAddress.value = uprnValid.toString

          And("I click the Select button")
          click on BusinessChooseYourAddressPage.select

          And("I enter \"A1\" in the vehicle registration number field")
          VehicleLookupPage.vehicleRegistrationNumber enter registrationNumberValid

          And("I enter \"12345678910\" in the V5C document reference number field")
          VehicleLookupPage.documentReferenceNumber enter referenceNumberValid

          And("I click the find vehicle details button")
          click on VehicleLookupPage.findVehicleDetails

          And("I enter \"10000\" in the vehicle mileage field")
          DisposePage.mileage enter mileageValid

          And("I select \"01\" from the date of disposal day dropdown")
          DisposePage.dateOfDisposalDay select dateOfDisposalDayValid

          And("I select \"March\" from the date of disposal month dropdown")
          DisposePage.dateOfDisposalMonth select dateOfDisposalMonthValid

          And("I enter \"2014\" in the date of disposal year field")
          DisposePage.dateOfDisposalYear select dateOfDisposalYearValid

          And("I enter \"viv.richards@emailprovider.co.uk\" in the email address field")
          DisposePage.emailAddress enter emailValid

          And("I select \"I have the consent of the current keeper to dispose of this vehicle\"")
          click on DisposePage.consent

          When("I click the dispose button")
          click on DisposePage.dispose

          Then("I should see \"Dispose a vehicle into the motor trade: summary \"")
          page.text should include("Dispose a vehicle into the motor trade: summary")
        }
      }
    }
}
