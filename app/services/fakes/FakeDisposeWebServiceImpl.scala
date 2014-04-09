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
import FakeVehicleLookupWebService._
import FakeDisposeWebServiceImpl._

class FakeDisposeWebServiceImpl extends DisposeWebService {
  override def callDisposeService(request: DisposeRequest): Future[Response] = Future {
    val responseAsJson = Json.toJson(disposeResponseSuccess)
    Logger.debug(s"FakeVehicleLookupWebService callVehicleLookupService with: $responseAsJson")
    new FakeResponse(status = 200, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
  }
}

object FakeDisposeWebServiceImpl {
  val transactionIdValid = "1234"
  val auditIdValid = "7575"

  val disposeResponseSuccess =
    DisposeResponse(success = true,
      message = "Fake Web Dispose Service - Good response",
      transactionId = transactionIdValid,
      registrationNumber = registrationNumberValid,
      auditId = auditIdValid)
}