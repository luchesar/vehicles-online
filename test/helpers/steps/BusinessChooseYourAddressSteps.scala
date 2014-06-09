package helpers.steps

import cucumber.api.java.en.{And, Then, When, Given}
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.webbrowser.{WebBrowserDSL, WebBrowserDriver}
import org.openqa.selenium.WebDriver
import org.scalatest.Matchers
import pages.common.Languages._
import pages.disposal_of_vehicle._
import mappings.disposal_of_vehicle.SetupTradeDetails._
import services.fakes.FakeAddressLookupService._

final class BusinessChooseYourAddressSteps(webBrowserDriver: WebBrowserDriver) extends WebBrowserDSL with Matchers {

  implicit val webDriver = webBrowserDriver.asInstanceOf[WebDriver]

  @Given("^the site is being viewed in the English Language$")
  def `the site is being viewed in the English Language`() = {
    go to BeforeYouStartPage
    CookieFactoryForUISpecs.
      withLanguageEn(). // Set language to English.
      setupTradeDetails()
    go to BusinessChooseYourAddressPage
    page.title should equal(BusinessChooseYourAddressPage.title)
  }

  @Given("^the site is being viewed in the Welsh Language$")
  def `the site is being viewed in the Welsh Language`() = {
    go to BeforeYouStartPage
    CookieFactoryForUISpecs.
      withLanguageCy(). // Set language to Welsh.
      setupTradeDetails()
    go to BusinessChooseYourAddressPage
    page.title should equal(BusinessChooseYourAddressPage.titleCy)
  }

  @When("^I select the 'Cymraeg' button in the footer$")
  def `I select the 'Cymraeg' button in the footer`() = {
    click on cymraeg
  }

  @When("^I select the 'English' button in the footer$")
  def `I select the 'English' button in the footer`() = {
    click on english
  }

  @Then("^the site is displayed in Welsh$")
  def `the site is displayed in Welsh`() = {
    page.title should equal(BusinessChooseYourAddressPage.titleCy)
  }

  @Then("^the site is displayed in English$")
  def `the site is displayed in English`() = {
    page.title should equal(BusinessChooseYourAddressPage.title)
  }

  @And("^I can continue my transaction without the need to re-enter previously submitted data$")
  def `I can continue my transaction without the need to re-enter data`() = {
    val setupTradeDetails = webDriver.manage().
      getCookieNamed(SetupTradeDetailsCacheKey).
      getValue

    setupTradeDetails should include(PostcodeValid)
    setupTradeDetails should include(TraderBusinessNameValid)
  }

  @And("^the language button in the footer will change to 'English'$")
  def `the language button in the footer will change to 'English'`() = {
    hasCymraeg should equal(false)
    hasEnglish should equal(true)
  }

  @And("^the language button in the footer will change to 'Cymraeg'$")
  def `the language button in the footer will change to 'Cymraeg'`() = {
    hasCymraeg should equal(true)
    hasEnglish should equal(false)
  }
}
