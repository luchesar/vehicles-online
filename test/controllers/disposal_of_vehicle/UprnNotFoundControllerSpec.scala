package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import org.scalatest.{Matchers, WordSpec}
import helpers.disposal_of_vehicle.{SetUpTradeDetailsPage, UprnNotFoundPage}

class UprnNotFoundControllerSpec extends WordSpec with Matchers {

  "UprnNotFound - Controller" should {

    "present" in new WithApplication {
      // Arrange

      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.UprnNotFound.present(request)

      // Assert
      status(result) should equal(OK)
    }
  }
}