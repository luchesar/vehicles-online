package views.disposal_of_vehicle

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle.{EnterAddressManuallyPage, BusinessChooseYourAddressPage, SetUpTradeDetailsPage, VehicleLookupPage}

class BusinessChooseYourAddressIntegrationSpec extends Specification with Tags {
  "business_choose_your_address Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.setupCache()
      browser.goTo(BusinessChooseYourAddressPage.url)

      // Assert
      titleMustEqual(BusinessChooseYourAddressPage.title)
    }

    "go to the next page when correct data is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.happyPath(browser)

      // Assert
      titleMustEqual(VehicleLookupPage.title)
    }

    "redirect to setupTradeDetails page when no dealer name cached" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo(BusinessChooseYourAddressPage.url)

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "display validation error messages when addressSelected is not in the list" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.sadPath(browser)

      //Assert
      checkNumberOfValidationErrors(2)
    }

    "display previous page when back link is clicked" in new WithBrowser with BrowserMatchers {
      // Arrange
      SetUpTradeDetailsPage.setupCache()
      browser.goTo(BusinessChooseYourAddressPage.url)

      // Act
      browser.click("#backButton")

      //Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "go to the enter address manually page when the enter address manually link is clicked" in new WithBrowser with BrowserMatchers {
      // Arrange
      SetUpTradeDetailsPage.setupCache()
      browser.goTo(BusinessChooseYourAddressPage.url)

      // Act
      browser.click("#enterAddressManuallyButton")

      // Assert
      titleMustEqual(EnterAddressManuallyPage.title)
    }
  }
}