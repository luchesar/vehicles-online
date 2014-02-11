package helpers.change_of_address

import play.api.test.TestBrowser
import models.domain.change_of_address.V5cSearchConfirmationModel
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import helpers.change_of_address.COAValidValues._

object LoginPagePopulate extends WordSpec with Matchers with Mockito {
  def loginPagePopulate(browser: TestBrowser) = {
    browser.goTo("/login-page")
    browser.fill("#username") `with` usernameValid
    browser.fill("#password") `with` passwordValid
    browser.submit("button[type='submit']")
  }
}