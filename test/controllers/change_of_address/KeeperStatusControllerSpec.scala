package controllers.change_of_address

import org.specs2.mutable._
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.change_of_address


class KeeperStatusControllerSpec extends Specification with Tags {

  "KeeperStatus - Controller" should {


    "present" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.KeeperStatus.present(request)

      // Assert
      status(result) mustEqual OK
    }


    "redirect to next page after the i'm a private individual button is clicked" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.KeeperStatus.submit(request)

      // Assert
      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustEqual (Some("/verify-identity")) //TODO page should redirect to p3verifyidentity
    }

  }

}