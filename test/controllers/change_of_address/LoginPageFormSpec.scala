package controllers.change_of_address

import org.specs2.mutable._

class LoginPageFormSpec extends Specification with Tags {

  "loginPage form" should {

    def loginPageForm(username: String, password: String) = {
      LoginPage.loginPageForm.bind(
        Map(
          "username" -> username,
          "password" -> password
        )
      )
    }
    /*
    "Accept when all fields filled in correctly" in {
      
      loginPageForm(username = "test", password = "test").fold(
        formWithErrors => {
          failure("Should be success instead failure has occured")
        },
        f => {
          f.username must equalTo("test")
          f.password must equalTo("test")
        }
      )
      
    }    */
    
  }
}
