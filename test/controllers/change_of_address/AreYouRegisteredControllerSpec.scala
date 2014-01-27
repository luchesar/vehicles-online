package controllers.change_of_address

import org.specs2.mutable._
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.change_of_address
import org.scalatest.{Matchers, WordSpec}


class AreYouRegisteredControllerSpec  extends WordSpec with Matchers {

  "AreYouRegistered - Controller" should {


    "present" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.AreYouRegistered.present(request)

      // Assert
      status(result) should equal(OK)
    }


    "redirect to next page after the button is clicked" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.AreYouRegistered.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal(Some("/sign-in-provider")) //TODO update with next page url
    }

  }

}