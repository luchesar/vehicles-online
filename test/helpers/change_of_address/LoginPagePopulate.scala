package helpers.change_of_address

import play.api.test.TestBrowser
import helpers.change_of_address.Helper._

object LoginPagePopulate {
  def happyPath(browser: TestBrowser) = {
    browser.goTo("/login-page")
    browser.fill("#username") `with` usernameValid
    browser.fill("#password") `with` passwordValid
    browser.submit("button[type='submit']")
  }
}