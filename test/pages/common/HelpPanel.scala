package pages.common

import helpers.webbrowser.{Element, WebBrowserDSL}
import mappings.common.Help._
import org.openqa.selenium.WebDriver

object HelpPanel extends WebBrowserDSL {
  def help(implicit driver: WebDriver): Element = find(id(HelpId)).get
}
