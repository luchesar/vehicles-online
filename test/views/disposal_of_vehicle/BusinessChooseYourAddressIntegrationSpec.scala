package views.disposal_of_vehicle

import org.specs2.mutable.Specification
import helpers.webbrowser.TestHarness
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup
import pages.common.ErrorPanel
import java.util.concurrent.TimeUnit
import helpers.disposal_of_vehicle.Helper._
import helpers.UiSpec

class BusinessChooseYourAddressIntegrationSpec extends UiSpec with TestHarness {
  "Business choose your address - Integration" should {

    "be presented" in new WebBrowser {
      // Arrange & Act
      CacheSetup.setupTradeDetails()
      go to BusinessChooseYourAddressPage.url

      //Assert
      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "go to the next page when correct data is entered" in new WebBrowser {
      // Arrange & Act
      CacheSetup.setupTradeDetails()
      BusinessChooseYourAddressPage.happyPath

      // Assert
      assert(page.title equals VehicleLookupPage.title)
    }

    "go to the manual address entry page when manualAddressButton is clicked" in new WebBrowser {
      // Arrange
      CacheSetup.setupTradeDetails()
      go to BusinessChooseYourAddressPage.url

      //Act
      click on BusinessChooseYourAddressPage.manualAddress

      // Assert
      assert(page.title equals EnterAddressManuallyPage.title)
    }

    "display previous page when back link is clicked" in new WebBrowser {
      // Arrange
      CacheSetup.setupTradeDetails()
      go to BusinessChooseYourAddressPage.url

      // Act
      click on BusinessChooseYourAddressPage.back

      //Assert
      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when no traderBusinessName is cached" in new WebBrowser {
      // Arrange & Act
      go to BusinessChooseYourAddressPage.url

      // Assert
      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "display validation error messages when addressSelected is not in the list" in new WebBrowser {
      // Arrange & Act
      CacheSetup.setupTradeDetails()
      BusinessChooseYourAddressPage.sadPath

      //Assert
      assert(ErrorPanel.numberOfErrors equals 1)
    }


    "check number of options in drop down" in new WebBrowser {
      // Arrange
      SetupTradeDetailsPage.happyPath

      //Act
      assert(BusinessChooseYourAddressPage.getListCount equals 1)
    }

//TODO discuss below two tests to check integrity
    "display address dropdown when address service returns addresses" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath

      // Assert
      //browser.waitUntil[Boolean](duration, TimeUnit.SECONDS) {.
      val result = webDriver.getPageSource
      result.contains("No addresses found for that postcode")  must beEqualTo(false)

      assert(BusinessChooseYourAddressPage.getListCount equals 1)
    }

/* //TODO need to amend below test not to use fake microservice
    "display message when address service returns no addresses" in new WebBrowser {
      // Arrange
      SetupTradeDetailsPage.happyPath(webDriver,traderPostcode = postcodeNoResults)

      // Assert
      //browser.waitUntil[Boolean](duration, TimeUnit.SECONDS) {
      val result = webDriver.getPageSource
      result.contains("No addresses found for that postcode")  must beEqualTo(true)


      assert(BusinessChooseYourAddressPage.getListCount equals 0)
      }*/
    }
  }
