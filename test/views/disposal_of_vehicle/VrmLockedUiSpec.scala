package views.disposal_of_vehicle

import helpers.tags.UiTag
import pages.disposal_of_vehicle.VrmLockedPage._
import helpers.UiSpec
import helpers.webbrowser.TestHarness
import pages.disposal_of_vehicle._
import org.openqa.selenium.{By, WebElement, WebDriver}
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import mappings.disposal_of_vehicle.RelatedCacheKeys

final class VrmLockedUiSpec extends UiSpec with TestHarness {
  "go to page" should {
    "display the page" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      CookieFactoryForUISpecs.bruteForcePreventionViewModel()
      go to VrmLockedPage

      page.title should equal(VrmLockedPage.title)
    }

    "contain the hidden csrfToken field" taggedAs UiTag in new WebBrowser {
      go to VrmLockedPage
      val csrf: WebElement = webDriver.findElement(By.name(services.csrf_prevention.CsrfPreventionAction.TokenName))
      csrf.getAttribute("type") should equal("hidden")
      csrf.getAttribute("name") should equal(services.csrf_prevention.CsrfPreventionAction.TokenName)
      csrf.getAttribute("value").size > 0 should equal(true)
    }

  }

  "newDisposal button" should {
    "redirect to vehiclelookup" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to VrmLockedPage

      click on newDisposal

      page.title should equal(VehicleLookupPage.title)
    }

    "redirect to setuptradedetails when no trade details are cached" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      CookieFactoryForUISpecs.bruteForcePreventionViewModel()
      go to VrmLockedPage

      click on newDisposal

      page.title should equal(SetupTradeDetailsPage.title)
    }

    "remove redundant cookies" taggedAs UiTag in new WebBrowser {
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
    "redirect to beforeyoustart" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to VrmLockedPage

      click on exit

      page.title should equal(BeforeYouStartPage.title)
    }

    "remove redundant cookies" taggedAs UiTag in new WebBrowser {
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
      dealerDetails().
      bruteForcePreventionViewModel()
}
