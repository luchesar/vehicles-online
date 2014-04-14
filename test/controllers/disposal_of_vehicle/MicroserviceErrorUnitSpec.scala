package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import mappings.disposal_of_vehicle.Dispose._
import models.domain.disposal_of_vehicle.{DisposeRequest, DisposeResponse}
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup
import org.mockito.Mockito._
import org.mockito.Matchers._
import helpers.UnitSpec
import services.dispose_service.{DisposeServiceImpl, DisposeWebService, DisposeService}
import services.fakes.{FakeDisposeWebServiceImpl, FakeResponse}
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json.Json
import ExecutionContext.Implicits.global
import services.DateServiceImpl
import services.fakes.FakeDateServiceImpl._
import services.fakes.FakeDisposeWebServiceImpl._

class MicroServiceErrorUnitSpec extends UnitSpec {
  "Micro-service error - Controller" should {
    "present" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = controllers.disposal_of_vehicle.MicroServiceError.present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }
  }
}