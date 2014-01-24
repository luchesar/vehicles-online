package controllers.change_of_address

import org.specs2.mutable._
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.change_of_address


class AuthenticationControllerSpec extends Specification with Tags {

  "Authentication - Controller" should {


    "present" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.Authentication.present(request)

      // Assert
      status(result) mustEqual OK
    }


//    "redirect to next page after the button is clicked" in new WithApplication {
//      // Arrange
//      val request = FakeRequest().withSession()
//
//      // Act
//      val result = change_of_address.Authentication.submit(request)
//
//      // Assert
//      status(result) mustEqual SEE_OTHER
//      redirectLocation(result) mustEqual (Some("/authentication")) //TODO update with next page url
//    }

  }

}