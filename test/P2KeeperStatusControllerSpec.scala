package controllers

import org.specs2.mutable._
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._


class P2KeeperStatusControllerSpec extends Specification with Tags {
  "P2KeeperStatus - Controller" should {


    "present" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.P2KeeperStatus.present(request)

      // Assert
      status(result) mustEqual OK
    }


    "redirect to next page after i'm a private individual button clicked" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.P2KeeperStatus.submit(request)

      // Assert
      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustEqual (Some("/p3verifyidentity"))
    }


  }
}
