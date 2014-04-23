package views.disposal_of_vehicle

import pages.disposal_of_vehicle._
import helpers.webbrowser.TestHarness
import pages.common.ErrorPanel
import pages.common.Accessibility
import helpers.disposal_of_vehicle.Helper._
import helpers.UiSpec
import SetupTradeDetailsPage.happyPath

class SetUpTradeDetailsIntegrationSpec extends UiSpec with TestHarness  {

  "SetUpTradeDetails Integration" should {

    "be presented" in new WebBrowser {
      go to SetupTradeDetailsPage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "go to the next page when correct data is entered" in new WebBrowser {
      happyPath()

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display two summary validation error messages when no details are entered" in new WebBrowser {
      happyPath(traderBusinessName = "", traderPostcode = "")

      assert(ErrorPanel.numberOfErrors equals 2)
    }

    "display three validation error messages when a valid postcode is entered with no business name" in new WebBrowser  {
      happyPath(traderBusinessName = "")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a valid postcode is entered with a business name less than min length" in new WebBrowser {
      happyPath(traderBusinessName = "m")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a valid business name is entered with no postcode" in new WebBrowser {
      happyPath(traderPostcode = "")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a valid business name is entered with a postcode less than min length" in new WebBrowser {
      happyPath(traderPostcode = "a")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a valid business name is entered with a postcode containing an incorrect format" in new WebBrowser {
      happyPath(traderPostcode = "SAR99")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing <" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + ">")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing >" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + ">")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing !" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + "!")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing =" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + "=")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing $" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + "$")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing /" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + "/")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing ?" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + "?")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing #" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + "#")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing \"" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + "\"")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing [" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + "]")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing £" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + "£")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing %" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + "%")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing ^" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + "^")

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing _" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + "_")
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display no error messages when a trader name is entered containing a number" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + "1")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing a captial letter" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + "W")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing '" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + "'")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing +" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + "+")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing -" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + "-")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing (" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + "(")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing )" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + ")")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing ." in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + ".")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing &" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + "&")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing ," in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + ",")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing @" in new WebBrowser {
      happyPath(traderBusinessName = traderBusinessNameValid + "@")

      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "add saria required attribute to trader name field when required field not input" in new WebBrowser {
      happyPath(traderBusinessName = "" )

      assert(Accessibility.ariaRequiredPresent("dealerName") equals true)
    }

    "add aria invalid attribute to trader name field when invalid characters input on field" in new WebBrowser {
      happyPath(traderBusinessName = "$£%&" )

      assert(Accessibility.ariaInvalidPresent("dealerName") equals true)
    }

    "add aria required attribute to trader postcode field when required field not input" in new WebBrowser {
      happyPath(traderPostcode = "" )

      assert(Accessibility.ariaRequiredPresent("dealerPostcode") equals true)
    }

    "add aria invalid attribute to trader postcode field when invalid characters input on field" in new WebBrowser {
      happyPath(traderPostcode = "$£%&" )

      assert(Accessibility.ariaInvalidPresent("dealerPostcode") equals true)
    }
  }
}

