package views.disposal_of_vehicle

import org.specs2.mutable.Specification
import helpers.webbrowser.TestHarness
import pages.disposal_of_vehicle._

class BusinessChooseYourAddressIntegrationSpec extends Specification  with TestHarness  {
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
      BusinessChooseYourAddressPage.sadPath

      //Assert
      assert(ErrorPanel.numberOfErrors equals 1)
    }

/* TODO - need to convert the three tests below

    "check number of options in drop down" in new WebBrowser {
      // Arrange
      SetupTradeDetailsPage.happyPath

      //Act
      val result = browser.find("#disposal_businessChooseYourAddress_addressSelect option").size

      result must beEqualTo(3) //this test currently looks at hardcoded service, however options within the drop down list are counted correctly
    }


    "display address dropdown when address service returns addresses" in new WebBrowser {
      // Arrange
      SetupTradeDetailsPage.happyPath

      // Assert
      browser.waitUntil[Boolean](duration, TimeUnit.SECONDS) {
        // TODO split this into two helpers, pass in the boolean on the first helper, pass in expected size on the second helper.
        browser.pageSource.contains("No addresses found for that postcode.") must beEqualTo(false) // No "not found" message present.

        val dropdownCount = browser.find("#disposal_businessChooseYourAddress_addressSelect").size
        dropdownCount mustEqual (1) // The dropdown is present.
      }
    }


    "display message when address service returns no addresses" in new WithBrowser with BrowserMatchers {
      // Arrange
      SetUpTradeDetailsPage.setupCache(traderPostcode = postcodeInvalid)

      // Act
      browser.goTo(BusinessChooseYourAddressPage.url)

      // Assert
      browser.waitUntil[Boolean](duration, TimeUnit.SECONDS) {
        browser.pageSource.contains("No addresses found for that postcode.") must beEqualTo(true) // "not found" message present.

        val dropdownCount = browser.find("#disposal_businessChooseYourAddress_addressSelect").size
        dropdownCount mustEqual (0) // The dropdown is not present.
      }
    }*/
  }
}