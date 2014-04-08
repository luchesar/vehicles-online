package services.fakes

import models.domain.disposal_of_vehicle._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import models.domain.disposal_of_vehicle.DisposeResponse
import services.dispose_service.DisposeWebService
import play.api.libs.ws.Response
import scala.Some
import play.api.libs.json.Json
import play.api.Logger
import FakeDisposeWebServiceImpl._

class FakeDisposeWebServiceImpl extends DisposeWebService {
  override def callDisposeService(request: DisposeRequest): Future[Response] = Future {
    val disposeResponse =
      DisposeResponse(success = true,
        message = "Fake Web Dispose Service - Good response",
        transactionId = "1234",
        registrationNumber = registrationNumberValid,
        auditId = "7575")
    val responseAsJson = Json.toJson(disposeResponse)
    Logger.debug(s"FakeVehicleLookupWebService callVehicleLookupService with: $responseAsJson")
    new FakeResponse(status = 200, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
  }
}

object FakeDisposeWebServiceImpl {
  val registrationNumberValid = "AB12AWR"
}