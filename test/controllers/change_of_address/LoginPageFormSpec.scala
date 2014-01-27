package controllers.change_of_address

import org.scalatest.WordSpec
import org.scalatest.Matchers
import app.ChangeOfAddress._

class LoginPageFormSpec extends WordSpec with Matchers {

  "loginPage form" should {

    val usernameValid = "testUsername"
    val passwordValid = "testPassword"

    def loginPageForm(username: String, password: String) = {
      LoginPage.loginPageForm.bind(
        Map(
          usernameId -> username,
          passwordId -> password
        )
      )
    }

    "Accept when only mandatory fields are filled in" in {
      loginPageForm(username = usernameValid, password = passwordValid).fold(
        formWithErrors => {
          fail("Should be success instead failure has occured")
        },
        f => {
          f.username should equal(usernameValid)
          f.password should equal(passwordValid)
        }
      )
    }
  }
}