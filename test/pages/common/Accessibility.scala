package pages.common

import helpers.webbrowser.{WebBrowserDSL, Page}
import org.openqa.selenium.{By, WebDriver}

object Accessibility extends Page with WebBrowserDSL {
  override val url: String = ""
  override val title: String = ""

  def ariaRequiredPresent(implicit driver: WebDriver, controlName: String) : Boolean = driver.findElement(By.id(controlName)).getAttribute("aria-required").toBoolean
  def ariaInvalidPresent(implicit driver: WebDriver, controlName: String) : Boolean = driver.findElement(By.id(controlName)).getAttribute("aria-invalid").toBoolean

}
