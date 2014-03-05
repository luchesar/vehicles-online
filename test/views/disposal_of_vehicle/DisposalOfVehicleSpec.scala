package views.disposal_of_vehicle

import org.scalatest.{BeforeAndAfterAll, Matchers, GivenWhenThen, FeatureSpec}
import pages.disposal_of_vehicle._
import helpers.WebDriverFactory
import play.api.Play
import play.api.test.{Helpers, TestServer}

class DisposalOfVehicleSpec extends FeatureSpec with GivenWhenThen with Matchers with BeforeAndAfterAll
    with BeforeYouStartPage with SetupTradeDetailsPage with BusinessChangeYourAddressPage with VehicleLookupPage with DisposePage with DisposeSuccessPage {

  lazy val app: TestServer = {
    TestServer(9000)
  }

  override def beforeAll() {
    app.start()
  }

  override def afterAll() {
    app.stop()
  }

  feature("Dispose of a vehicle to trade") {

    info("As a vehicle trader")
    info("I want to dispose of a vehicle for a customer")
    info("So they will be removed from the vehicle record as current keeper")
    info("")
    /*
        scenario("Before you start page should be presented") {
           implicit val browser = WebDriverFactory.webDriver

           Given("I am on the vehicles online prototype site")
           go to BeforeYouStartPage

           //When("I click the Start now button to begin the transaction")
           //click on BeforeYouStartPage.startNow

           Then("I should see \"Dispose a vehicle into the motor trade: start\"")
           //page.text should include("Dispose a vehicle into the motor trade: set-up")

           page.title should equal(BeforeYouStartPage.title)

         }


        scenario("Go to motor trade set-up page when next button is clicked") {
          implicit val browser = WebDriverFactory.webDriver

          Given("I am on the vehicles online prototype site")
          go to BeforeYouStartPage

          When("I click the Start now button to begin the transaction")
          click on BeforeYouStartPage.startNow

          Then("I should see \"Dispose a vehicle into the motor trade: set-up\"")
          //page.text should include("Dispose a vehicle into the motor trade: set-up")

          page.title should equal(SetupTradeDetailsPage.title)

          browser.quit()
        }

         scenario("Setup trade details page") {

           Given("I am on the setup trade details page")
           go to SetupTradeDetailsPage

           And("I enter \"Car Giant\" in the business name field")
           SetupTradeDetailsPage.dealerName enter "Car Giant"

           And("I enter \"CM8 1QJ\" in the business postcode field")
           SetupTradeDetailsPage.dealerPostcode enter "CM8 1QJ"

           When("I click the Look-up button")
           click on SetupTradeDetailsPage.lookup

           Then("I should see \"Business: Choose your address\"")
           page.text should include("Business: Choose your address")
         }

         scenario("Choose your address page") {

           Given("I am on the choose your address page")
           go to BusinessChangeYourAddressPage

           And("I select \"1, OLIVERS DRIVE, WITHAM, CM8 1QJ\"")
           BusinessChangeYourAddressPage.chooseAddress select "100090327899"

           When("I click the Select button")
           click on BusinessChangeYourAddressPage.select

           Then("I should see \"Dispose a vehicle into the motor trade: vehicle\"")
           page.text should include("Dispose a vehicle into the motor trade: vehicle")
         }

         scenario("Vehicle lookup page") {

           Given("I am on the vehicle lookup page")
           go to VehicleLookupPage

           And("I enter \"12345678910\" in the V5C document reference number field")
           VehicleLookupPage.v5cReferenceNumber enter "12345678910"

           And("I enter \"A1\" in the vehicle registration number field")
           VehicleLookupPage.v5cRegistrationNumber enter "A1"

           And("I enter \"Viv Richards\" in the full keeper name field")
           VehicleLookupPage.v5cKeeperName enter "Viv Richards"

           And("I enter \"SA4 4DW\" in the keeper postcode field")
           VehicleLookupPage.v5cPostcode enter "SA4 4DW"

           When("I click the find vehicle details button")
           click on VehicleLookupPage.findVehicleDetails

           Then("I should see \"Dispose a vehicle into the motor trade: confirm\"")
           page.text should include("Dispose a vehicle into the motor trade: confirm")
         }

         scenario("Dispose page") {

           Given("I am on the dispose page")
           go to DisposePage

           And("I enter \"10000\" in the vehicle mileage field")
           DisposePage.mileage enter "10000"

           And("I select \"01\" from the date of disposal day dropdown")
           DisposePage.dateOfDisposalDay select "1"

           And("I select \"March\" from the date of disposal month dropdown")
           DisposePage.dateOfDisposalMonth select "3"

           And("I enter \"2014\" in the date of disposal year field")
           DisposePage.dateOfDisposalYear enter "2014"

           And("I select \"I have the consent of the current keeper to dispose of this vehicle\" from the date of disposal month dropdown")
           click on DisposePage.consent

           And("I enter \"viv.richards@emailprovider.co.uk\" in the email address field")
           DisposePage.emailAddress enter "viv.richards@emailprovider.co.uk"

           When("I click the dispose button")
           click on DisposePage.dispose

           Then("I should see \"Dispose a vehicle into the motor trade: summary\"")
           page.text should include("Dispose a vehicle into the motor trade: summary")
         }

         scenario("Dispose confirmation page") {

           Given("I am on the dispose confirmation page")
           go to DisposeSuccessPage

           When("I click the new disposal button")
           click on DisposeSuccessPage.newDisposal

           Then("I should see \"Dispose a vehicle into the motor trade: vehicle\"")
           page.text should include("Dispose a vehicle into the motor trade: vehicle")
         }
     */
  }
}
