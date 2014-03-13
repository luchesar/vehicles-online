package views.disposal_of_vehicle

import org.specs2.mutable.Specification
import pages.disposal_of_vehicle._
import helpers.webbrowser.TestHarness
import pages.disposal_of_vehicle.EnterAddressManuallyPage._

class EnterAddressManuallyIntegrationSpec extends Specification  with TestHarness {

  "EnterAddressManually integration" should {

    "be presented" in new WebBrowser {
      // Arrange & Act
      CacheSetup.setupTradeDetails()
      go to EnterAddressManuallyPage.url

      // Assert
      assert(page.title equals EnterAddressManuallyPage.title)
    }

    "accept and redirect when all fields are input with valid entry" in new WebBrowser {
      // Arrange & Act
      EnterAddressManuallyPage.happyPath

      // Assert
      assert(page.title equals VehicleLookupPage.title)
    }

    "accept when only mandatory fields only are input" in new WebBrowser {
      // Arrange & Act
      EnterAddressManuallyPage.happyPathMandatoryFieldsOnly

      // Assert
      assert(page.title equals VehicleLookupPage.title)
    }

    "display validation error messages when no details are entered" in new WebBrowser {
      // Arrange
      CacheSetup.setupTradeDetails()
      go to EnterAddressManuallyPage.url

      // Act
      click on EnterAddressManuallyPage.next

      // Assert
      assert(ErrorPanel.numberOfErrors equals 5)
    }

    "display validation error messages when a blank line 1 is entered" in new WebBrowser {
      // Arrange
      CacheSetup.setupTradeDetails()
      go to EnterAddressManuallyPage.url
      EnterAddressManuallyPage.addressLine1.value = ""
      EnterAddressManuallyPage.addressLine2.value = line2Valid
      EnterAddressManuallyPage.addressLine3.value = line3Valid
      EnterAddressManuallyPage.addressLine4.value = line4Valid
      EnterAddressManuallyPage.postcode.value = postcodeValid

      //Act
      click on EnterAddressManuallyPage.next

      // Assert
      assert(ErrorPanel.numberOfErrors equals 2)
    }

    "display validation error messages when line 1 is entered which is greater than max length" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      go to EnterAddressManuallyPage.url
      EnterAddressManuallyPage.addressLine1.value = ("a" * 76)
      EnterAddressManuallyPage.addressLine2.value = line2Valid
      EnterAddressManuallyPage.addressLine3.value = line3Valid
      EnterAddressManuallyPage.addressLine4.value = line4Valid
      EnterAddressManuallyPage.postcode.value = postcodeValid

      //Act
      click on EnterAddressManuallyPage.next

      // Assert
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display validation error messages when a blank postcode is entered" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      go to EnterAddressManuallyPage.url
      EnterAddressManuallyPage.addressLine1.value = line1Valid
      EnterAddressManuallyPage.addressLine2.value = line2Valid
      EnterAddressManuallyPage.addressLine3.value = line3Valid
      EnterAddressManuallyPage.addressLine4.value = line4Valid
      EnterAddressManuallyPage.postcode.value = ""

      //Act
      click on EnterAddressManuallyPage.next

      // Assert
      assert(ErrorPanel.numberOfErrors equals 3)
    }

    "display validation error messages when a postcode is entered containing special characters" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      go to EnterAddressManuallyPage.url
      EnterAddressManuallyPage.addressLine1.value = line1Valid
      EnterAddressManuallyPage.addressLine2.value = line2Valid
      EnterAddressManuallyPage.addressLine3.value = line3Valid
      EnterAddressManuallyPage.addressLine4.value = line4Valid
      EnterAddressManuallyPage.postcode.value = "SA99 1D!"

      //Act
      click on EnterAddressManuallyPage.next

      // Assert
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display validation error messages when a postcode is entered containing letters only" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      go to EnterAddressManuallyPage.url
      EnterAddressManuallyPage.addressLine1.value = line1Valid
      EnterAddressManuallyPage.addressLine2.value = line2Valid
      EnterAddressManuallyPage.addressLine3.value = line3Valid
      EnterAddressManuallyPage.addressLine4.value = line4Valid
      EnterAddressManuallyPage.postcode.value = "SQWER"

      //Act
      click on EnterAddressManuallyPage.next

      // Assert
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display validation error messages when a postcode is entered containing numbers only" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      go to EnterAddressManuallyPage.url
      EnterAddressManuallyPage.addressLine1.value = line1Valid
      EnterAddressManuallyPage.addressLine2.value = line2Valid
      EnterAddressManuallyPage.addressLine3.value = line3Valid
      EnterAddressManuallyPage.addressLine4.value = line4Valid
      EnterAddressManuallyPage.postcode.value = "12345"

      //Act
      click on EnterAddressManuallyPage.next

      // Assert
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display validation error messages when a postcode is entered in an incorrect format" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      go to EnterAddressManuallyPage.url
      EnterAddressManuallyPage.addressLine1.value = line1Valid
      EnterAddressManuallyPage.addressLine2.value = line2Valid
      EnterAddressManuallyPage.addressLine3.value = line3Valid
      EnterAddressManuallyPage.addressLine4.value = line4Valid
      EnterAddressManuallyPage.postcode.value = "SA99 1B1"

      //Act
      click on EnterAddressManuallyPage.next

      // Assert
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display validation error messages when a postcode is entered less than min length" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      go to EnterAddressManuallyPage.url
      EnterAddressManuallyPage.addressLine1.value = line1Valid
      EnterAddressManuallyPage.addressLine2.value = line2Valid
      EnterAddressManuallyPage.addressLine3.value = line3Valid
      EnterAddressManuallyPage.addressLine4.value = line4Valid
      EnterAddressManuallyPage.postcode.value = "SA"

      //Act
      click on EnterAddressManuallyPage.next

      // Assert
      assert(ErrorPanel.numberOfErrors equals 2)
    }
  }
}
