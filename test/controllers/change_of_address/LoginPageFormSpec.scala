package controllers.change_of_address

import helpers.change_of_address.Helper._
import helpers.UnitSpec
import helpers.UnitSpec

class LoginPageFormSpec extends UnitSpec {

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