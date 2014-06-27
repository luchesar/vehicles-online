package controllers.common

import helpers.UnitSpec
import play.api.test.FakeRequest
import play.mvc.Http.Status._

class HealthCheckSpec extends UnitSpec {

  "requests to /healthcheck" should {

    "GET request should return 200" in {
      val result = new HealthCheck().respond(FakeRequest("GET", "/healthcheck"))
      whenReady(result) (_.header.status should equal(OK))
    }
  }

}
