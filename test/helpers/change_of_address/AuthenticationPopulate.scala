package helpers.change_of_address

import play.api.test.TestBrowser
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import helpers.change_of_address.COAValidValues._

object AuthenticationPopulate extends WordSpec with Matchers with Mockito {
  def authenticationPopulate(browser: TestBrowser) = {
    browser.goTo("/authentication")
    browser.fill("#PIN") `with` pinValid
    browser.submit("button[type='submit']")
  }
}