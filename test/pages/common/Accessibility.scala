package pages.common

import helpers.webbrowser.{WebBrowserDSL, Page}
import org.openqa.selenium.{By, WebDriver}

object Accessibility extends Page with WebBrowserDSL {
  override val url: String = ""
  override val title: String = ""

  def ariaRequiredPresent(controlName: String)(implicit driver: WebDriver) : Boolean = driver.findElement(By.id(controlName)).getAttribute("aria-required").toBoolean
  def ariaInvalidPresent(controlName: String)(implicit driver: WebDriver) : Boolean = driver.findElement(By.id(controlName)).getAttribute("aria-invalid").toBoolean

}
