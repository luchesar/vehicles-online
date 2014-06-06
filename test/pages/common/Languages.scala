package pages.common

import helpers.webbrowser.WebBrowserDSL
import org.openqa.selenium.WebDriver
import mappings.common.Languages._

object Languages extends WebBrowserDSL{
  def hasCymraeg(implicit driver: WebDriver): Boolean = find(id(CyId)).isDefined
  def hasEnglish(implicit driver: WebDriver): Boolean = find(id(EnId)).isDefined
}
