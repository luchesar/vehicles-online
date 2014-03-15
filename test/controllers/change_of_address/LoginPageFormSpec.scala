package controllers.change_of_address

import org.scalatest.WordSpec
import org.scalatest.Matchers
import org.scalatest.mock.MockitoSugar
import helpers.change_of_address.Helper._

class LoginPageFormSpec extends WordSpec with Matchers with MockitoSugar {

  "LoginPage form" should {

    val mockWebService = mock[services.LoginWebService]
    val loginPage = new LoginPage(mockWebService)

    def form(username: String, password: String) = {
      loginPage.loginPageForm.bind(
        Map(
          mappings.change_of_address.LoginPage.usernameId -> username,
          mappings.change_of_address.LoginPage.passwordId -> password
        )
      )
    }

    "Accept when only mandatory fields are filled in" in {
      val model = form(username = usernameValid, password = passwordValid).get
      model.username should equal(usernameValid)
      model.password should equal(passwordValid)
    }
  }
}