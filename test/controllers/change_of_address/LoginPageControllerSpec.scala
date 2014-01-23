package controllers.change_of_address

import org.specs2.mutable._
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.change_of_address


class LoginPageControllerSpec extends Specification with Tags {

  "LoginPage - Controller" should {


    "present" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.LoginPage.present(request)

      // Assert
      status(result) mustEqual OK
    }
  }

//    "redirect to next page after the i'm a private individual button is clicked" in new WithApplication {
//      // Arrange
//      val request = FakeRequest().withSession()
//
//      // Act
//      val result = change_of_address.KeeperStatus.submit(request)
//
//      // Assert
//      status(result) mustEqual SEE_OTHER
//      redirectLocation(result) mustEqual (Some("/keeper-status")) //TODO page should redirect to p3verifyidentity
//    }


}