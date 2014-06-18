package helpers.steps.disposal_of_vehicle

import app.ConfigProperties._
import helpers.webbrowser.{WebBrowserDSL, WebBrowserDriver}
import org.scalatest.Matchers
import org.openqa.selenium.WebDriver
import cucumber.api.java.en._
import pages.disposal_of_vehicle._
import pages.common.ErrorPanel
import scala.collection.JavaConversions._
import common.{ClearTextClientSideSessionFactory, NoCookieFlags, EncryptedClientSideSessionFactory}
import common.CookieImplicits.RequestCookiesAdapter
import utils.helpers.{CookieNameHashing, Sha1Hash, AesEncryption, CookieEncryption}
import models.domain.disposal_of_vehicle.VehicleLookupFormModel
import scala.Some
import play.api.mvc.Cookie

// TODO please find a better name for this class as it is the same as a file in the level above!
final class CommonSteps(webBrowserDriver: WebBrowserDriver) extends WebBrowserDSL with Matchers {

  implicit val clientSideSessionFactory = {
    implicit val cookieFlags = new NoCookieFlags()
    val testRemote = getProperty("test.remote", default = false)
    if (testRemote)  {
      implicit val cookieEncryption = new AesEncryption with CookieEncryption
      implicit val cookieNameHashing = new Sha1Hash with CookieNameHashing
      new EncryptedClientSideSessionFactory()
    }
    else {
      new ClearTextClientSideSessionFactory()
    }
  }

  implicit val webDriver = webBrowserDriver.asInstanceOf[WebDriver]

  def start() = {
    go to BeforeYouStartPage
    page.title should equal(BeforeYouStartPage.title)
    click on BeforeYouStartPage.startNow
  }

  def setupTraderDetails() = {
    page.title should equal(SetupTradeDetailsPage.title)
    SetupTradeDetailsPage.traderName enter "Big Motors Limited"
    SetupTradeDetailsPage.traderPostcode enter "AA99 1AA"
    click on SetupTradeDetailsPage.lookup
  }

  def chooseYourAddressManual() = {
    page.title should equal(BusinessChooseYourAddressPage.title)
    click on BusinessChooseYourAddressPage.manualAddress
  }

  def enterAddressManually() = {
    page.title should equal(EnterAddressManuallyPage.title)
    EnterAddressManuallyPage.addressBuildingNameOrNumber enter "1 Long Road"
    EnterAddressManuallyPage.addressPostTown enter "Swansea"
    click on EnterAddressManuallyPage.next
  }

  @When("""^this is submitted along with any other mandatory information$""")
  def this_is_submitted_along_with_any_other_mandatory_information() = {
    submit()
  }

  @Then("""^the document reference number "(.*)" is retained$""")
  def the_document_reference_number_is_retained(docRefNo:String) = {
    def unquoteString(quotedString: String): String = {
      val stringWithoutEnclosingQuotes = quotedString.drop(1).dropRight(1)
      stringWithoutEnclosingQuotes.replace("\\\"", "\"")
    }

    val webDriverCookies = webDriver.manage().getCookies.toSet
    val playCookies = webDriverCookies.map(c => Cookie(c.getName, unquoteString(c.getValue)))

    playCookies.getModel[VehicleLookupFormModel] match {
      case Some(VehicleLookupFormModel(referenceNumber, _)) =>
        referenceNumber should equal(docRefNo)
      case None =>
        assert(false, "No valid cookie exists for this model")
    }
  }

  @Then("""^the vehicle reference mark "(.*)" is retained$""")
  def the_vehicle_reference_mark_is_retained(refMark:String) = {
    //
  }

  @Then("""^the next step in the dispose transaction "(.*)" is shown$""")
  def the_next_step_in_the_dispose_transaction_is_shown(title:String) = {
    page.title should equal(title)
  }

  @Then("""^a single error message "(.*)" is displayed$""")
  def a_single_error_message_msg_is_displayed(message: String) = {
    ErrorPanel.numberOfErrors should equal(1)
    ErrorPanel.text should include(message)
  }

  @Then("""^the dispose transaction does not proceed past the "(.*)" step$""")
  def the_dispose_transaction_does_not_proceed_past_the_step(title:String) = {
    page.title should equal(title)
  }
}