package views.disposal_of_vehicle

import pages.disposal_of_vehicle._
import helpers.webbrowser.TestHarness
import pages.common.ErrorPanel
import pages.common.Accessibility
import helpers.disposal_of_vehicle.Helper._
import helpers.UiSpec
import org.openqa.selenium.By

class SetUpTradeDetailsIntegrationSpec extends UiSpec with TestHarness  {

  "SetUpTradeDetails Integration" should {
    "be presented" in new WebBrowser {
      go to SetupTradeDetailsPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "go to the next page when correct data is entered" in new WebBrowser {
      SetupTradeDetailsPage.happyPath

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display five validation error messages when no details are entered" in new WebBrowser {
      SetupTradeDetailsPage.happyPath( webDriver,"","")

      assert(ErrorPanel.numberOfErrors equals 5)

    }

    "display two validation error messages when a valid postcode is entered with no business name" in new WebBrowser  {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = "")

      assert(ErrorPanel.numberOfErrors equals 2)
    }

    "display one validation error message when a valid postcode is entered with a business name less than min length" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = "m")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display three validation error messages when a valid business name is entered with no postcode" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderPostcode = "")

      assert(ErrorPanel.numberOfErrors equals 3)
    }

    "display two validation error messages when a valid business name is entered with a postcode less than min length" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderPostcode = "a")

      assert(ErrorPanel.numberOfErrors equals 2)
    }

    "display one validation error message when a valid business name is entered with a postcode containing an incorrect format" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderPostcode = "SAR99")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing <" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + ">")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing >" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + ">")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing !" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "!")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing =" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "=")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing $" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "$")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing /" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "/")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing ?" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "?")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing #" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "#")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing \"" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "\"")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing [" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "]")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing £" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "£")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing $" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "$")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing %" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "%")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing ^" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "^")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing _" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "_")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display no error messages when a trader name is entered containing a number" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "1")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing a captial letter" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "W")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing '" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "'")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing +" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "+")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing -" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "-")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing (" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "(")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing )" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + ")")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing ." in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + ".")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing &" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "&")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing ," in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + ",")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing @" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "@")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "add aria required attribute to trader name field when required field not input" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver,traderBusinessName = "" )

      assert(Accessibility.ariaRequiredPresent(webDriver,"dealerName") equals true)
    }

    "add aria invalid attribute to trader name field when invalid characters input on field" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver,traderBusinessName = "$£%&" )

      assert(Accessibility.ariaInvalidPresent(webDriver,"dealerName") equals true)
    }

    "add aria required attribute to trader postcode field when required field not input" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver,traderPostcode = "" )

      assert(Accessibility.ariaRequiredPresent(webDriver,"dealerPostcode") equals true)
    }

    "add aria invalid attribute to trader postcode field when invalid characters input on field" in new WebBrowser {
      SetupTradeDetailsPage.happyPath(webDriver,traderPostcode = "$£%&" )

      assert(Accessibility.ariaInvalidPresent(webDriver,"dealerPostcode") equals true)
    }
  }
}

