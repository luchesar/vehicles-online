package controllers.change_of_address

import org.scalatest.WordSpec
import org.scalatest.Matchers

class AuthenticationFormSpecScalaTest extends WordSpec with Matchers {

  "loginPage form" should {
    val PINFormID = "PIN"
    val PINValid = "123456"

    def authenticationFiller(PIN: String) = {
      Authentication.authenticationForm.bind(
        Map(
          PINFormID -> PIN
        )
      )
    }

    "Accept when all fields filled in correctly" in {
      authenticationFiller(PIN = PINValid).fold(
        formWithErrors => {
          fail("Should be success instead failure has occured")
        },
        f => {
          f.PIN should equal(PINValid)
        }
      )
    }
  }
}