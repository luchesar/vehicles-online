package helpers.change_of_address

import play.api.test.TestBrowser
import models.domain.change_of_address.V5cSearchConfirmationModel
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import helpers.ValidValues._

object AuthenticationPopulate extends WordSpec with Matchers with Mockito {

  def authenticationPopulate(browser: TestBrowser) = {
    browser.goTo("/authentication")
    browser.fill("#PIN") `with` pinValid
    browser.submit("button[type='submit']")
  }
}