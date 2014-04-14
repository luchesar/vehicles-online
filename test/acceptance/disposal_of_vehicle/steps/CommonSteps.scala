package acceptance.disposal_of_vehicle.steps

import cucumber.api.java.en.Then
import pages.common.ErrorPanel
import org.scalatest.Matchers
import org.openqa.selenium.WebDriver
import helpers.webbrowser.{WebBrowserDSL, WebBrowserDriver}

class CommonSteps(webBrowserDriver:WebBrowserDriver) extends WebBrowserDSL with Matchers {

  implicit val webDriver = webBrowserDriver.asInstanceOf[WebDriver]

  @Then("""^a single error message is displayed "(.*)"$""")
  def a_single_error_message_is_displayed(message:String) = {
    ErrorPanel.numberOfErrors should equal(1)
    ErrorPanel.text should include(message)
  }

  @Then("""^they are taken to the "(.*)" page$""")
  def they_are_taken_to_the_next_step_in_the_transaction(title:String) = {
    page.title should equal(title)
  }

  @Then("""^they remain on the "(.*)" page$""")
  def they_remain_at_the_current_stage_in_the_transaction(title:String) = {
    page.title should equal(title)
  }
}
