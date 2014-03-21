package views.disposal_of_vehicle

import pages.disposal_of_vehicle._
import helpers.webbrowser.TestHarness
import helpers.disposal_of_vehicle.CacheSetup
import pages.common.ErrorPanel
import helpers.UiSpec
import helpers.disposal_of_vehicle.Helper._

class EnterAddressManuallyIntegrationSpec extends UiSpec with TestHarness {

  "EnterAddressManually integration" should {

    "be presented" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      go to EnterAddressManuallyPage.url

      assert(page.title equals EnterAddressManuallyPage.title)
    }

    "accept and redirect when all fields are input with valid entry" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath

      assert(page.title equals VehicleLookupPage.title)
    }

    "accept when only mandatory fields only are input" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPathMandatoryFieldsOnly

      assert(page.title equals VehicleLookupPage.title)
    }

    "display validation error messages when no details are entered" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.sadPath

      assert(ErrorPanel.numberOfErrors equals 5)
    }

    "display validation error messages when a blank line1 is entered" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line1 = "")

      assert(ErrorPanel.numberOfErrors equals 2)
    }


    "display validation error messages when line1 is entered which is greater than max length" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line1 = ("a" * 76))

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display validation error messages when a blank postcode is entered" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, postcode = "")

      assert(ErrorPanel.numberOfErrors equals 3)
    }

    "display validation error messages when a postcode is entered containing special characters" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, postcode = "SA99 1D!")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display validation error messages when a postcode is entered containing letters only" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, postcode = "SQWER")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display validation error messages when a postcode is entered containing numbers only" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, postcode = "12345")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display validation error messages when a postcode is entered in an incorrect format" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, postcode = "SA99 1B1")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display validation error messages when a postcode is entered less than min length" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, postcode = "SA")

      assert(ErrorPanel.numberOfErrors equals 2)
    }

    "display one validaton error message when an invalid character is entered into address line 1 !" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line1= line1Valid + "?")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 1 %" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line1= line1Valid + "%")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 1 /" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line1= line1Valid + "/")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 1 +" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line1= line1Valid + "+")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 1 £" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line1= line1Valid + "£")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 1 ^" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line1= line1Valid + "^")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "accept and redirect when line1 contains ," in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line1 = line1Valid + ",")

      assert(page.title equals VehicleLookupPage.title)
    }

    "accept and redirect when line1 contains ." in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line1 = line1Valid + ".")

      assert(page.title equals VehicleLookupPage.title)
    }


    "display one validaton error message when an invalid character is entered into address line 2 !" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line2= line2Valid + "?")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 2 %" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line2= line2Valid + "%")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 2 /" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line2= line2Valid + "/")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 2 +" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line2= line2Valid + "+")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 2 £" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line2= line2Valid + "£")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 2 ^" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line2= line2Valid + "^")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "accept and redirect when line2 contains ," in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line2 = line2Valid + ",")

      assert(page.title equals VehicleLookupPage.title)
    }

    "accept and redirect when line2 contains ." in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line2 = line2Valid + ".")

      assert(page.title equals VehicleLookupPage.title)
    }

    "display one validaton error message when an invalid character is entered into address line 3 !" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line3= line3Valid + "?")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 3 %" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line3= line3Valid + "%")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 3 /" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line3= line3Valid + "/")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 3 +" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line3= line3Valid + "+")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 3 £" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line3= line3Valid + "£")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 3 ^" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line3= line3Valid + "^")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "accept and redirect when line3 contains ," in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line3 = line3Valid + ",")

      assert(page.title equals VehicleLookupPage.title)
    }

    "accept and redirect when line3 contains ." in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line3 = line3Valid + ".")

      assert(page.title equals VehicleLookupPage.title)
    }

    "display one validaton error message when an invalid character is entered into address line 4 !" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line4= line4Valid + "?")

      assert(ErrorPanel.numberOfErrors equals 1)
    }


    "display one validaton error message when an invalid character is entered into address line 4 %" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line4= line4Valid + "%")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 4 /" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line4= line4Valid + "/")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 4 +" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line4= line4Valid + "+")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 4 £" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line4= line4Valid + "£")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validaton error message when an invalid character is entered into address line 4 ^" in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line4= line4Valid + "^")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "accept and redirect when line4 contains ," in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line4 = line4Valid + ",")

      assert(page.title equals VehicleLookupPage.title)
    }

    "accept and redirect when line4 contains ." in new WebBrowser {
      CacheSetup.setupTradeDetails()
      EnterAddressManuallyPage.happyPath(webDriver, line4 = line4Valid + ".")

      assert(page.title equals VehicleLookupPage.title)
    }

  }
}
