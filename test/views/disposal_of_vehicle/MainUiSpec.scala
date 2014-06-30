package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.tags.UiTag
import helpers.webbrowser.{TestGlobal, TestHarness}
import pages.common.AlternateLanguages.{hasCymraeg, hasEnglish}
import pages.common.Feedback.mailto
import pages.disposal_of_vehicle.BeforeYouStartPage
import play.api.test.FakeApplication

final class MainUiSpec extends UiSpec with TestHarness {
  "go to page" should {
    "display the 'Cymraeg' language button and not the 'English' language button when the play language cookie has value 'en'" taggedAs UiTag ignore new WebBrowser {
      go to BeforeYouStartPage // By default will load in English.
      CookieFactoryForUISpecs.withLanguageEn()
      go to BeforeYouStartPage

      hasCymraeg should equal(true)
      hasEnglish should equal(false)
    }

    "display the 'English' language button and not the 'Cymraeg' language button when the play language cookie has value 'cy'" taggedAs UiTag ignore new WebBrowser {
      go to BeforeYouStartPage // By default will load in English.
      CookieFactoryForUISpecs.withLanguageCy()
      go to BeforeYouStartPage

      hasCymraeg should equal(false)
      hasEnglish should equal(true)
    }

    "display the 'Cymraeg' language button and not the 'English' language button when the play language cookie does not exist (assumption that the browser default language is English)" taggedAs UiTag ignore new WebBrowser {
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

    "not display prototype message when config set to false" taggedAs UiTag in new WebBrowser(app = fakeAppWithPrototypeFalse) {
      go to BeforeYouStartPage

      page.source should not include """<div class="prototype">"""
    }
  }

  private val fakeAppWithPrototypeFalse = FakeApplication(
    withGlobal = Some(TestGlobal),
    additionalConfiguration = Map("prototype.disclaimer" -> "false"))
}