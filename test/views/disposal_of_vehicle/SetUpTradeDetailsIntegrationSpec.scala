package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.webbrowser.TestHarness
import mappings.disposal_of_vehicle.SetupTradeDetails
import pages.common.Accessibility
import pages.common.ErrorPanel
import pages.disposal_of_vehicle.SetupTradeDetailsPage.happyPath
import pages.disposal_of_vehicle._

final class SetUpTradeDetailsIntegrationSpec extends UiSpec with TestHarness  {

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
      happyPath(traderBusinessName = "", traderBusinessPostcode = "")
      assert(ErrorPanel.numberOfErrors equals 2)
    }

    "add aria required attribute to trader name field when required field not input" in new WebBrowser {
      happyPath(traderBusinessName = "" )
      assert(Accessibility.ariaRequiredPresent(SetupTradeDetails.TraderNameId) equals true)
    }

    "add aria invalid attribute to trader name field when invalid characters input on field" in new WebBrowser {
      happyPath(traderBusinessName = "$£%&" )
      assert(Accessibility.ariaInvalidPresent(SetupTradeDetails.TraderNameId) equals true)
    }

    "add aria required attribute to trader postcode field when required field not input" in new WebBrowser {
      happyPath(traderBusinessPostcode = "" )
      assert(Accessibility.ariaRequiredPresent(SetupTradeDetails.TraderPostcodeId) equals true)
    }

    "add aria invalid attribute to trader postcode field when invalid characters input on field" in new WebBrowser {
      happyPath(traderBusinessPostcode = "$£%&" )
      assert(Accessibility.ariaInvalidPresent(SetupTradeDetails.TraderPostcodeId) equals true)
    }
  }
}
