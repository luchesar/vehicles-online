package views.disposal_of_vehicle

import helpers.tags.UiTag
import pages.disposal_of_vehicle.DisposePage._
import helpers.UiSpec
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.webbrowser.{WebDriverFactory, TestHarness}
import org.openqa.selenium.{By, WebElement, WebDriver}
import pages.common.ErrorPanel
import pages.disposal_of_vehicle._
import services.fakes.FakeDateServiceImpl._
import mappings.disposal_of_vehicle.Dispose._
import org.scalatest.time.{Seconds, Span}
import org.scalatest.concurrent.Eventually._

final class DisposeIntegrationSpec extends UiSpec with TestHarness {
  "go to page" should {
    "display the page" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      go to DisposePage

      page.title should equal(title)
    }

    "display the progress of the page" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      go to DisposePage

      page.source.contains("Step 5 of 6") should equal(true)
    }

    "redirect when no vehicleDetailsModel is cached" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      CookieFactoryForUISpecs.dealerDetails()

      go to DisposePage

      page.title should equal(VehicleLookupPage.title)
    }

    "redirect when no businessChooseYourAddress is cached" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      CookieFactoryForUISpecs.vehicleDetailsModel()

      go to DisposePage

      page.title should equal(SetupTradeDetailsPage.title)
    }

    "redirect when no traderBusinessName is cached" taggedAs UiTag in new WebBrowser {
      go to DisposePage

      page.title should equal(SetupTradeDetailsPage.title)
    }

    "contain the hidden csrfToken field" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      go to DisposePage
      val csrf: WebElement = webDriver.findElement(By.name(services.csrf_prevention.CsrfPreventionAction.TokenName))
      csrf.getAttribute("type") should equal("hidden")
      csrf.getAttribute("name") should equal(services.csrf_prevention.CsrfPreventionAction.TokenName)
      csrf.getAttribute("value").size > 0 should equal(true)
    }

  }

  "dispose button" should {
    "display DisposeSuccess page on correct submission" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup().
        vehicleLookupFormModel()

      happyPath

      page.title should equal(DisposeSuccessPage.title)
    }

    // This test needs to run with javaScript enabled.
    "display DisposeSuccess page on correct submission with javascript enabled" taggedAs UiTag in new WebBrowser(webDriver = WebDriverFactory.webDriver(targetBrowser = "htmlUnit", javascriptEnabled = true)) {
      go to BeforeYouStartPage
      cacheSetup().
        vehicleLookupFormModel()

      happyPath


      // We want to wait for the javascript to execute and redirect to the next page. For build servers we may need to
      // wait longer than the default.
      val timeout: Span = scaled(Span(2, Seconds))
      implicit val patienceConfig: PatienceConfig = PatienceConfig(timeout = timeout)

      eventually {page.title should equal(DisposeSuccessPage.title)}
    }

    // This test needs to run with javaScript enabled.
    "display DisposeSuccess page on correct submission when a user auto populates the date of disposal with javascript enabled" taggedAs UiTag in new WebBrowser(webDriver = WebDriverFactory.webDriver(targetBrowser = "htmlUnit", javascriptEnabled = true)) {
      go to BeforeYouStartPage
      cacheSetup().
        vehicleLookupFormModel()
      go to DisposePage

      click on useTodaysDate

      dateOfDisposalDay.value should equal(DateOfDisposalDayValid)
      dateOfDisposalMonth.value should equal(DateOfDisposalMonthValid)
      dateOfDisposalYear.value should equal(DateOfDisposalYearValid)

      click on consent
      click on lossOfRegistrationConsent
      click on dispose


      // We want to wait for the javascript to execute and redirect to the next page. For build servers we may need to
      // wait longer than the default.
      val timeout: Span = scaled(Span(2, Seconds))
      implicit val patienceConfig: PatienceConfig = PatienceConfig(timeout = timeout)

      eventually {page.title should equal(DisposeSuccessPage.title)}
    }

    "display validation errors when no data is entered" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      sadPath

      ErrorPanel.numberOfErrors should equal(3)
    }

    "display validation errors when month and year are input but no day" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to DisposePage
      dateOfDisposalMonth select DateOfDisposalMonthValid
      dateOfDisposalYear select DateOfDisposalYearValid

      click on consent
      click on lossOfRegistrationConsent
      click on dispose

      ErrorPanel.numberOfErrors should equal(1)
    }

    "display validation errors when day and year are input but no month" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to DisposePage
      dateOfDisposalDay select DateOfDisposalDayValid
      dateOfDisposalYear select DateOfDisposalYearValid

      click on consent
      click on lossOfRegistrationConsent
      click on dispose

      ErrorPanel.numberOfErrors should equal(1)
    }

    "display validation errors when day and month are input but no year" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to DisposePage
      dateOfDisposalDay select DateOfDisposalDayValid
      dateOfDisposalMonth select DateOfDisposalMonthValid

      click on consent
      click on lossOfRegistrationConsent
      click on dispose

      ErrorPanel.numberOfErrors should equal(1)
    }

    "display validation errors when day month and year are not input but all other mandatory fields have been" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to DisposePage

      click on consent
      click on lossOfRegistrationConsent
      click on dispose

      ErrorPanel.numberOfErrors should equal(1)
    }
  }

  "back button" should {
    "display previous page" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to DisposePage

      click on back

      page.title should equal(VehicleLookupPage.title)
    }
  }

  "javascript disabled" should {
    // This test needs to run with javaScript enabled.
    "not display the Use Todays Date checkbox" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup().
        vehicleLookupFormModel()

      webDriver.getPageSource shouldNot contain(TodaysDateOfDisposal)
    }
  }

  "use today's date" should {
    // This test needs to run with javaScript enabled.
    "fill in the date fields" taggedAs UiTag in new WebBrowser(webDriver = WebDriverFactory.webDriver(targetBrowser = "htmlUnit", javascriptEnabled = true)) {
      go to BeforeYouStartPage
      cacheSetup()
      go to DisposePage

      click on useTodaysDate

      dateOfDisposalDay.value should equal(DateOfDisposalDayValid)
      dateOfDisposalMonth.value should equal(DateOfDisposalMonthValid)
      dateOfDisposalYear.value should equal(DateOfDisposalYearValid)
    }
  }

  private def cacheSetup()(implicit webDriver: WebDriver) = {
    CookieFactoryForUISpecs.
      dealerDetails().
      vehicleDetailsModel()
  }
}
