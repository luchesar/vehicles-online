package helpers.steps

import cucumber.api.java.en.{When, Given, Then}
import org.openqa.selenium.{WebDriver, Cookie}
import helpers.webbrowser.{WebBrowserDriver, WebBrowserDSL}
import org.scalatest.Matchers
import pages.disposal_of_vehicle.{BeforeYouStartPage, SetupTradeDetailsPage}
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import common.ClientSideSessionFactory

class TrackingIdSteps(webBrowserDriver: WebBrowserDriver) extends WebBrowserDSL with Matchers {

  implicit val webDriver = webBrowserDriver.asInstanceOf[WebDriver]

  private val trackingIdTestValue = "trackingIdTestValue"

  @Then("^trackingId should be accessible with non empty value$")
  def `trackingId should be accessible with non empty value`() = {
//    TODO uncomment these after we start running the acceptance tests with the real application
//    val trackingIdCookie = webDriver.manage().getCookieNamed(ClientSideSessionFactory.SessionIdCookieName)
//    trackingIdCookie.getValue should not be null
//    trackingIdCookie.getExpiry should be(null)
//    trackingIdCookie.getPath should be("/")

  }

  @Given("^the motor trader has already been in the website and has trackingId assigned$")
  def `the motor trader has already been in the website and has trackingId assigned`() {
    go to BeforeYouStartPage
    webDriver.manage().addCookie(new Cookie(ClientSideSessionFactory.TrackingIdCookieName, trackingIdTestValue))
  }

  @When("^going to the website again$")
  def `going to the website again`() = {
    CookieFactoryForUISpecs.setupTradeDetails().
      dealerDetails()
    go to SetupTradeDetailsPage
  }

  @Then("^the trackingId should stay the same$")
  def `the trackingId should stay the same`() = {
    val trackingIdCookie = webDriver.manage().getCookieNamed(ClientSideSessionFactory.TrackingIdCookieName)
    trackingIdCookie.getValue should be(trackingIdTestValue)
  }
}
