package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.tags.UiTag
import helpers.webbrowser.TestHarness
import mappings.disposal_of_vehicle.RelatedCacheKeys
import org.openqa.selenium.WebDriver
import pages.common.AlternateLanguages._
import pages.disposal_of_vehicle.BeforeYouStartPage._
import pages.disposal_of_vehicle.{BeforeYouStartPage, SetupTradeDetailsPage}

final class BeforeYouStartIntegrationSpec extends UiSpec with TestHarness {
  "go to page" should {
    "display the page" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage

      page.title should equal(BeforeYouStartPage.title)
    }

    "remove redundant cookies (needed for when a user exits the service and comes back)" taggedAs UiTag in new WebBrowser {
      def cacheSetup()(implicit webDriver: WebDriver) =
        CookieFactoryForUISpecs.setupTradeDetails().
          businessChooseYourAddress().
          enterAddressManually().
          dealerDetails().
          vehicleDetailsModel().
          disposeFormModel().
          disposeTransactionId().
          vehicleRegistrationNumber()

      go to BeforeYouStartPage
      cacheSetup()
      go to BeforeYouStartPage

      // Verify the cookies identified by the full set of cache keys have been removed
      RelatedCacheKeys.FullSet.foreach(cacheKey => webDriver.manage().getCookieNamed(cacheKey) should equal(null))
    }

    "display the 'Cymraeg' language button and not the 'English' language button when the play language cookie has value 'en'" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage // By default will load in English.
      CookieFactoryForUISpecs.withLanguageEn()
      go to BeforeYouStartPage

      hasCymraeg should equal(true)
      hasEnglish should equal(false)
    }

    "display the 'English' language button and not the 'Cymraeg' language button when the play language cookie has value 'cy'" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage // By default will load in English.
      CookieFactoryForUISpecs.withLanguageCy()
      go to BeforeYouStartPage

      hasCymraeg should equal(false)
      hasEnglish should equal(true)
    }

    "display the 'Cymraeg' language button and not the 'English' language button when the play language cookie does not exist (assumption that the browser default language is English)" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage

      hasCymraeg should equal(true)
      hasEnglish should equal(false)
    }
  }

  "startNow button" should {
    "go to next page" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage

      click on startNow

      page.title should equal(SetupTradeDetailsPage.title)
    }
  }
}