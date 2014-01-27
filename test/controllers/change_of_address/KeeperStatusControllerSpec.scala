package controllers.change_of_address

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.change_of_address
import org.scalatest.{Matchers, WordSpec}

class KeeperStatusControllerSpec extends WordSpec with Matchers {

  "KeeperStatus - Controller" should {

    "present" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.KeeperStatus.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to next page after the i'm a private individual button is clicked" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = change_of_address.KeeperStatus.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some("/verify-identity"))
    }
  }
}