package helpers

import play.api.test.TestBrowser
import models.domain.change_of_address.{V5cSearchConfirmationModel, LoginConfirmationModel}
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito

object AuthenticationPopulateHelper extends WordSpec with Matchers with Mockito {
  val pinValid = "123456"

  def authenticationPopulate(browser: TestBrowser) = {
    browser.goTo("/authentication")
    browser.fill("#PIN") `with` pinValid
    browser.submit("button[type='submit']")
  }
}