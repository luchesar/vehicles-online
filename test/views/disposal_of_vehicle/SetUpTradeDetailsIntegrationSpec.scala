package views.disposal_of_vehicle

import pages.disposal_of_vehicle._
import helpers.webbrowser.TestHarness
import pages.common.ErrorPanel
import pages.common.Accessibility
import helpers.disposal_of_vehicle.Helper._
import helpers.UiSpec
import org.openqa.selenium.By

class SetUpTradeDetailsIntegrationSpec extends UiSpec with TestHarness  {

  "SetUpTradeDetails Integration" should {
    "be presented" in new WebBrowser {
      // Arrange & Act
      go to SetupTradeDetailsPage

      // Assert
      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "go to the next page when correct data is entered" in new WebBrowser {
      // Arrange
      SetupTradeDetailsPage.happyPath

      // Assert
      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display five validation error messages when no details are entered" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath( webDriver,"","")

      // Assert
      assert(ErrorPanel.numberOfErrors equals 5)

    }

    "display two validation error messages when a valid postcode is entered with no business name" in new WebBrowser  {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = "")

      // Assert
      assert(ErrorPanel.numberOfErrors equals 2)
    }

    "display one validation error message when a valid postcode is entered with a business name less than min length" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = "m")

      // Assert
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display three validation error messages when a valid business name is entered with no postcode" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderPostcode = "")

      // Assert
      assert(ErrorPanel.numberOfErrors equals 3)
    }

    "display two validation error messages when a valid business name is entered with a postcode less than min length" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderPostcode = "a")

      // Assert
      assert(ErrorPanel.numberOfErrors equals 2)
    }

    "display one validation error message when a valid business name is entered with a postcode containing an incorrect format" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderPostcode = "SAR99")

      // Assert
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing <" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + ">")

      // Assert
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing >" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + ">")

      // Assert
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing !" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "!")

      // Assert
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing =" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "=")

      // Assert
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing $" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "$")

      // Assert
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing /" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "/")

      // Assert
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing ?" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "?")

      // Assert
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing #" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "#")

      // Assert
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing ?" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "?")

      // Assert
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing [" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "]")

      // Assert
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display one validation error message when a trader name is entered containing ?" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "?")

      // Assert
      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display no error messages when a trader name is entered containing a number" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "1")

      // Assert
      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing a captial letter" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "W")

      // Assert
      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing +" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "+")

      // Assert
      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing -" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "-")

      // Assert
      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing (" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "(")

      // Assert
      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing )" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + ")")

      // Assert
      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing ." in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + ".")

      // Assert
      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing &" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "&")

      // Assert
      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing ," in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + ",")

      // Assert
      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "display no error messages when a trader name is entered containing @" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver, traderBusinessName = traderBusinessNameValid + "@")

      // Assert
      assert(page.title equals BusinessChooseYourAddressPage.title)
    }

    "add aria required attribute to trader name field when required field not input" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver,traderBusinessName = "" )

      // Assert
      assert(Accessibility.ariaRequiredPresent(webDriver,"dealerName") equals true)
    }

    "add aria invalid attribute to trader name field when invalid characters input on field" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver,traderBusinessName = "$£%&" )

      // Assert
      assert(Accessibility.ariaInvalidPresent(webDriver,"dealerName") equals true)
    }

    "add aria required attribute to trader postcode field when required field not input" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver,traderPostcode = "" )

      // Assert
      assert(Accessibility.ariaRequiredPresent(webDriver,"dealerPostcode") equals true)
    }

    "add aria invalid attribute to trader postcode field when invalid characters input on field" in new WebBrowser {
      // Arrange & Act
      SetupTradeDetailsPage.happyPath(webDriver,traderPostcode = "$£%&" )

      // Assert
      assert(Accessibility.ariaInvalidPresent(webDriver,"dealerPostcode") equals true)
    }
  }
}

