package views.disposal_of_vehicle

import pages.disposal_of_vehicle.VrmLockedPage._
import helpers.UiSpec
import helpers.webbrowser.TestHarness
import pages.disposal_of_vehicle._
import org.openqa.selenium.WebDriver
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import mappings.disposal_of_vehicle.RelatedCacheKeys

final class VrmLockedSpec extends UiSpec with TestHarness {
  "go to page" should {
    "display the page" in new WebBrowser {
      go to VrmLockedPage

      page.title should equal(VrmLockedPage.title)
    }
  }

  "newDisposal button" should {
    "redirect to vehiclelookup" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to VrmLockedPage

      click on newDisposal

      page.title should equal(VehicleLookupPage.title)
    }

    "redirect to setuptradedetails when no details are cached" in new WebBrowser {
      go to VrmLockedPage

      click on newDisposal

      page.title should equal(SetupTradeDetailsPage.title)
    }

    "remove redundant cookies" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to VrmLockedPage

      click on newDisposal

      // Verify the cookies identified by the dispose set of cache keys have been removed
      RelatedCacheKeys.DisposeSet.foreach(cacheKey => {
        webDriver.manage().getCookieNamed(cacheKey) should equal(null)
      })
    }
  }

  "exit button" should {
    "redirect to beforeyoustart" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to VrmLockedPage

      click on exit

      page.title should equal(BeforeYouStartPage.title)
    }

    "remove redundant cookies" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to VrmLockedPage

      click on exit

      // Verify the cookies identified by the full set of cache keys have been removed
      RelatedCacheKeys.FullSet.foreach(cacheKey => {
        webDriver.manage().getCookieNamed(cacheKey) should equal(null)
      })
    }
  }

  private def cacheSetup()(implicit webDriver: WebDriver) =
    CookieFactoryForUISpecs.
      setupTradeDetails().
      dealerDetails()
}
