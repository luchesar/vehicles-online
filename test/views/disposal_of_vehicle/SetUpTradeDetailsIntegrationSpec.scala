package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.webbrowser.TestHarness
import mappings.disposal_of_vehicle.SetupTradeDetails
import pages.common.Accessibility
import pages.common.ErrorPanel
import pages.disposal_of_vehicle.SetupTradeDetailsPage.happyPath
import pages.disposal_of_vehicle._

final class SetUpTradeDetailsIntegrationSpec extends UiSpec with TestHarness {
  "got to page" should {
    "display the page" in new WebBrowser {
      go to SetupTradeDetailsPage
      page.title should equal(SetupTradeDetailsPage.title)
    }
    "contain the hidden csrfToken field" in new WebBrowser {
      go to SetupTradeDetailsPage
      page.source should include("input type=\"hidden\" name=\"csrfToken\"")
    }
  }

  "lookup button" should {
    "go to the next page when correct data is entered" in new WebBrowser {
      happyPath()
      page.title should equal(BusinessChooseYourAddressPage.title)
    }

    "display two summary validation error messages when no details are entered" in new WebBrowser {
      happyPath(traderBusinessName = "", traderBusinessPostcode = "")
      ErrorPanel.numberOfErrors should equal(2)
    }

    "add aria required attribute to trader name field when required field not input" in new WebBrowser {
      happyPath(traderBusinessName = "")
      Accessibility.ariaRequiredPresent(SetupTradeDetails.TraderNameId) should equal(true)
    }

    "add aria required attribute to trader postcode field when required field not input" in new WebBrowser {
      happyPath(traderBusinessPostcode = "")
      Accessibility.ariaRequiredPresent(SetupTradeDetails.TraderPostcodeId) should equal(true)
    }
  }
}
