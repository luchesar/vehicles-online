package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.tags.UiTag
import helpers.webbrowser.{TestGlobal, TestHarness}
import mappings.disposal_of_vehicle.RelatedCacheKeys
import org.openqa.selenium.WebDriver
import pages.common.AlternateLanguages._
import pages.disposal_of_vehicle.BeforeYouStartPage._
import pages.disposal_of_vehicle.{BeforeYouStartPage, SetupTradeDetailsPage}
import play.api.test.FakeApplication

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

    "display the 'Cymraeg' language button and not the 'English' language button and mailto when the play language cookie does not exist (assumption that the browser default language is English)" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage

      hasCymraeg should equal(true)
      hasEnglish should equal(false)
    }

    "display mailto link that specifies a subject" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage

      val result = mailto.attribute("href").get
      result should include("mailto:")
      result should include("Subject=")
    }

    "display prototype message when config set to true" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage

      page.source should include("""<div class="prototype">""")
    }

    "not display prototype message when config set to false" taggedAs UiTag in new WebBrowser(app = fakeAppWithPrototypeFalse) {
      go to BeforeYouStartPage

      page.source should not include """<div class="prototype">"""
    }
  }

  "startNow button" should {
    "go to next page" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage

      click on startNow

      page.title should equal(SetupTradeDetailsPage.title)
    }
  }

  private val fakeAppWithPrototypeFalse = FakeApplication(
    withGlobal = Some(TestGlobal),
    additionalConfiguration = Map("prototype.disclaimer" -> "false"))
}