package pages.common

import helpers.webbrowser.{Element, WebBrowserDSL}
import org.openqa.selenium.WebDriver
import mappings.common.AlternateLanguages._

object AlternateLanguages extends WebBrowserDSL{
  def cymraeg(implicit driver: WebDriver): Element = find(id(CyId)).get
  def english(implicit driver: WebDriver): Element = find(id(EnId)).get

  def hasCymraeg(implicit driver: WebDriver): Boolean = find(id(CyId)).isDefined
  def hasEnglish(implicit driver: WebDriver): Boolean = find(id(EnId)).isDefined
}