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

    "add aria required attribute to trader name field when required field not input" in new WebBrowser {
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

