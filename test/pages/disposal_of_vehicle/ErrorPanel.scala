package pages.disposal_of_vehicle

import helpers.webbrowser.{WebBrowserDSL, Page}
import org.openqa.selenium.{By, WebDriver}

object ErrorPanel extends Page with WebBrowserDSL {
  override val url: String = ""
  override val title: String = ""

  def numberOfErrors(implicit driver: WebDriver): Int = driver.findElement(By.cssSelector(".validation-summary")).findElements(By.tagName("li")).size

}
