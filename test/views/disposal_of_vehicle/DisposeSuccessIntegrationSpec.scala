package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.tags.UiTag
import helpers.webbrowser.TestHarness
import mappings.common.PreventGoingToDisposePage.{DisposeOccurredCacheKey, PreventGoingToDisposePageCacheKey}
import org.openqa.selenium.{By, WebElement, WebDriver}
import pages.disposal_of_vehicle.DisposeSuccessPage._
import pages.disposal_of_vehicle._
import mappings.disposal_of_vehicle.RelatedCacheKeys

final class DisposeSuccessIntegrationSpec extends UiSpec with TestHarness {
  "go to page" should {
    "display the page" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      go to DisposeSuccessPage

      page.title should equal(DisposeSuccessPage.title)
    }

    "display the progress of the page" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      go to DisposeSuccessPage

      page.source.contains("Step 6 of 6") should equal(true)
    }

    "redirect when no details are cached" taggedAs UiTag in new WebBrowser {
      go to DisposeSuccessPage

      page.title should equal(SetupTradeDetailsPage.title)
    }

    "redirect when only DealerDetails are cached" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      CookieFactoryForUISpecs.dealerDetails()

      go to DisposeSuccessPage

      page.title should equal(VehicleLookupPage.title)
    }

    "redirect when only VehicleDetails are cached" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      CookieFactoryForUISpecs.vehicleDetailsModel()

      go to DisposeSuccessPage

      page.title should equal(SetupTradeDetailsPage.title)
    }

    "redirect when only DisposeDetails are cached" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      CookieFactoryForUISpecs.disposeFormModel()

      go to DisposeSuccessPage

      page.title should equal(SetupTradeDetailsPage.title)
    }

    "redirect when only DealerDetails and VehicleDetails are cached" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      CookieFactoryForUISpecs.
        dealerDetails().
        vehicleDetailsModel()

      go to DisposeSuccessPage

      page.title should equal(VehicleLookupPage.title)
    }

    "redirect when only DisposeDetails and VehicleDetails are cached" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      CookieFactoryForUISpecs.
        disposeFormModel().
        vehicleDetailsModel()

      go to DisposeSuccessPage

      page.title should equal(SetupTradeDetailsPage.title)
    }

    "redirect when only DisposeDetails and DealerDetails are cached" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      CookieFactoryForUISpecs.
        dealerDetails().
        disposeFormModel()

      go to DisposeSuccessPage

      page.title should equal(VehicleLookupPage.title)
    }

    "contain the hidden csrfToken field" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      go to DisposeSuccessPage
      val csrf: WebElement = webDriver.findElement(By.name(services.csrf_prevention.CsrfPreventionAction.tokenName))
      csrf.getAttribute("type") should equal("hidden")
      csrf.getAttribute("name") should equal(services.csrf_prevention.CsrfPreventionAction.tokenName)
      csrf.getAttribute("value").size > 0 should equal(true)
    }
  }

  "newDisposal button" should {
    "display vehicle lookup page" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      DisposeSuccessPage.happyPath

      page.title should equal(VehicleLookupPage.title)
    }

    "remove and retain cookies" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to DisposeSuccessPage

      click on newDisposal

      // Verify the cookies identified by the dispose set of cache keys have been removed
      RelatedCacheKeys.DisposeSet.foreach(cacheKey => {
        webDriver.manage().getCookieNamed(cacheKey) should equal(null)
      })

      // Verify the cookies identified by the trade details set of cache keys are present.
      RelatedCacheKeys.TradeDetailsSet.foreach(cacheKey => {
        webDriver.manage().getCookieNamed(cacheKey) should not equal null
      })

      // Verify that the back button prevention cookie is present
      webDriver.manage().getCookieNamed(PreventGoingToDisposePageCacheKey) should not equal null

      // Verify that the dispose occurred is present
      webDriver.manage().getCookieNamed(DisposeOccurredCacheKey) should not equal null
    }
  }

  "exit button" should {
    "display before you start page" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to DisposeSuccessPage

      click on exitDisposal

      page.title should equal(BeforeYouStartPage.title)
    }

    "remove redundant cookies" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to DisposeSuccessPage

      click on exitDisposal

      // Verify the cookies identified by the full set of cache keys have been removed
      RelatedCacheKeys.FullSet.foreach(cacheKey => {
        webDriver.manage().getCookieNamed(cacheKey) should equal(null)
      })
    }
  }

  private def cacheSetup()(implicit webDriver: WebDriver) =
    CookieFactoryForUISpecs.
      setupTradeDetails().
      businessChooseYourAddress().
      enterAddressManually().
      dealerDetails().
      vehicleDetailsModel().
      disposeFormModel().
      disposeTransactionId().
      vehicleRegistrationNumber()
}
