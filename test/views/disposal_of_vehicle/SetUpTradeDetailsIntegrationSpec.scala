package views.disposal_of_vehicle

import org.specs2.mutable.Specification
import pages.disposal_of_vehicle._
import helpers.webbrowser.TestHarness
import org.openqa.selenium.By

class SetUpTradeDetailsIntegrationSpec extends Specification with TestHarness  {

  "SetUpTradeDetails Integration" should {
   /* "be presented" in new WebBrowser {
      // Arrange & Act
      go to SetupTradeDetailsPage

      // Assert
      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "go to the next page when correct data is entered" in new WebBrowser {
      // Arrange
      go to SetupTradeDetailsPage
      SetupTradeDetailsPage.dealerName enter "Car Giant"
      SetupTradeDetailsPage.dealerPostcode enter "CM8 1QJ"

      // Act
      click on SetupTradeDetailsPage.lookup

      // Assert
      assert(page.title equals BusinessChangeYourAddressPage.title)
    }*/

    "display five validation error messages when no details are entered" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath

      // Assert
      assert(ErrorPanel.numberOfErrors equals 5)

    }


  }
}


/*
    "display two validation error messages when a valid postcode is entered with no business name" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.happyPath(browser, traderBusinessName = "")

      // Assert
      checkNumberOfValidationErrors(2)
    }

    "display one validation error message when a valid postcode is entered with a business name less than min length" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.happyPath(browser, traderBusinessName = "m")

      // Assert
      checkNumberOfValidationErrors(1)
    }

    "display one validation error message when a valid postcode is entered with a business name more than max length" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.happyPath(browser, traderBusinessName = "qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopq")

      // Assert

      checkNumberOfValidationErrors(1)
    }

    "display three validation error messages when a valid business name is entered with no postcode" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.happyPath(browser, traderPostcode = "")

      // Assert
      checkNumberOfValidationErrors(3)
    }

    "display two validation error messages when a valid business name is entered with a postcode less than min length" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.happyPath(browser, traderPostcode = "a")

      // Assert
      checkNumberOfValidationErrors(2)
    }

    "display two validation error messages when a valid business name is entered with a postcode more than max length" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.happyPath(browser, traderPostcode = "SA99 1DDD")

      // Assert
      checkNumberOfValidationErrors(2)
    }

    "display one validation error message when a valid business name is entered with a postcode containing an incorrect format" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.happyPath(browser, traderPostcode = "SAR99")

      // Assert
      checkNumberOfValidationErrors(1)
    }
  }
}*/