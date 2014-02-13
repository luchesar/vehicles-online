package helpers.change_of_address

import play.api.test.TestBrowser
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import helpers.change_of_address.Helper._

object AuthenticationPopulate {

  val url = "/are-you-registered"

  def happyPath(browser: TestBrowser) = {
    browser.goTo("/authentication")
    browser.fill("#PIN") `with` pinValid
    browser.submit("button[type='submit']")
  }
}