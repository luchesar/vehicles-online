package views.disposal_of_vehicle

import org.scalatest.{BeforeAndAfterAll, Matchers, GivenWhenThen, FeatureSpec}
import pages.disposal_of_vehicle._
import helpers.WebDriverFactory
import play.api.test.TestServer

class BeforeYouStartIntegrationSpec extends FeatureSpec with GivenWhenThen with Matchers with BeforeAndAfterAll
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

    scenario("Before you start page should be presented") {
      implicit val browser = WebDriverFactory.webDriver

      Given("I am on the vehicles online prototype site")
      go to BeforeYouStartPage

      Then("I should see \"Dispose a vehicle into the motor trade: start\"")

      page.title should equal(BeforeYouStartPage.title)

    }


    scenario("Go to the next page when the start now button is clicked on the before you start page") {
      implicit val browser = WebDriverFactory.webDriver

      Given("I am on the vehicles online prototype site")
      go to BeforeYouStartPage

      When("I click the Start now button to begin the transaction")
      click on BeforeYouStartPage.startNow

      Then("I should see \"Dispose a vehicle into the motor trade: set-up\"")

      page.title should equal(SetupTradeDetailsPage.title)

      browser.quit()

      
    }
  }
}