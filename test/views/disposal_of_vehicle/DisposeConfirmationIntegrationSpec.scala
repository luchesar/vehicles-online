package views.disposal_of_vehicle

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle.{DisposeConfirmationPage, SetUpTradeDetailsPage, DisposePage, VehicleLookupPage, BusinessChooseYourAddressPage}

class DisposeConfirmationIntegrationSpec extends Specification with Tags {
  "Dispose confirmation integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.setupCache
      BusinessChooseYourAddressPage.setupCache
      VehicleLookupPage.setupCache
      DisposePage.setupCache
      browser.goTo(DisposeConfirmationPage.url)

      // Check the page title is correct
      titleMustEqual(DisposeConfirmationPage.title)
    }

  "Redirect when no traderBusinessName is cached" in new WithBrowser with BrowserMatchers {
    // Arrange & Act
    browser.goTo(DisposeConfirmationPage.url)

    // Assert
    titleMustEqual(SetUpTradeDetailsPage.title)
  }

  "display previous page when back link is clicked" in new WithBrowser with BrowserMatchers {
    SetUpTradeDetailsPage.setupCache
    BusinessChooseYourAddressPage.setupCache
    VehicleLookupPage.setupCache
    DisposePage.setupCache
    browser.goTo(DisposeConfirmationPage.url)

    browser.click("#backButton")
    titleMustEqual(DisposePage.title)
  }

    "display vehicle lookup page when new disposal link is clicked" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.setupCache
      BusinessChooseYourAddressPage.setupCache
      VehicleLookupPage.setupCache
      DisposePage.setupCache
      browser.goTo(DisposeConfirmationPage.url)

      browser.click("#newDisposal")
      titleMustEqual(VehicleLookupPage.title)
    }
  }
}
