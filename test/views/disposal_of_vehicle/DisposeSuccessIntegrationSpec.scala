package views.disposal_of_vehicle

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle._

class DisposeSuccessIntegrationSpec extends Specification with Tags {
  "Dispose confirmation integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      DisposeFailurePage.cacheSetupHappyPath
      browser.goTo(DisposeSuccessPage.url)

      // Check the page title is correct
      titleMustEqual(DisposeSuccessPage.title)
    }

    "redirect when no details are cached" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo(DisposeSuccessPage.url)

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "redirect when only DealerDetails are cached" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      BusinessChooseYourAddressPage.setupCache()
      browser.goTo(DisposeSuccessPage.url)

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "redirect when only VehicleDetails are cached" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      VehicleLookupPage.setupVehicleDetailsModelCache()
      browser.goTo(DisposeSuccessPage.url)

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "redirect when only DisposeDetails are cached" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      DisposePage.setupDisposeFormModelCache()
      browser.goTo(DisposeSuccessPage.url)

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "redirect when only DealerDetails and VehicleDetails are cached" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.setupVehicleDetailsModelCache()
      browser.goTo(DisposeSuccessPage.url)

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "redirect when only DisposeDetails and VehicleDetails are cached" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      DisposePage.setupDisposeFormModelCache()
      VehicleLookupPage.setupVehicleDetailsModelCache()
      browser.goTo(DisposeSuccessPage.url)

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "redirect when only DisposeDetails and DealerDetails are cached" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      DisposePage.setupDisposeFormModelCache()
      BusinessChooseYourAddressPage.setupCache()
      browser.goTo(DisposeSuccessPage.url)

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "display vehicle lookup page when new disposal link is clicked" in new WithBrowser with BrowserMatchers {
      // Arrange
      DisposeFailurePage.cacheSetupHappyPath
      browser.goTo(DisposeSuccessPage.url)

      // Act
      browser.click("#newDisposal")

      //Assert
      titleMustEqual(VehicleLookupPage.title)
    }

  }
}
