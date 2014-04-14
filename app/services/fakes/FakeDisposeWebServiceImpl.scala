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
import play.api.http.Status._
import play.api.libs.ws.Response
import scala.Some

class FakeDisposeWebServiceImpl extends DisposeWebService {
  override def callDisposeService(request: DisposeRequest): Future[Response] = Future {
    val disposeResponse: DisposeResponse = {
      request.referenceNumber match {
        case `simulateMicroServiceUnavailable` => throw new RuntimeException("simulateMicroServiceUnavailable")
        case `simulateSoapEndpointFailure` => disposeResponseSoapEndpointFailure
        case _ => disposeResponseSuccess
      }
    }
    val responseAsJson = Json.toJson(disposeResponse)
    Logger.debug(s"FakeVehicleLookupWebService callVehicleLookupService with: $responseAsJson")
    new FakeResponse(status = OK, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
  }
}

object FakeDisposeWebServiceImpl {
  val transactionIdValid = "1234"
  val auditIdValid = "7575"
  val simulateMicroServiceUnavailable = "8" * 11
  val simulateSoapEndpointFailure = "9" * 11

  val disposeResponseSuccess =
    DisposeResponse(success = true,
      message = "Fake Web Dispose Service - Good response",
      transactionId = transactionIdValid,
      registrationNumber = registrationNumberValid,
      auditId = auditIdValid)

  val disposeResponseFailure =
    DisposeResponse(success = false,
      message = "Fake Web Dispose Service - Bad response",
      transactionId = transactionIdValid, // We should always get back a transaction id even for failure scenarios. Only exception is if the soap endpoint is down
      registrationNumber = "",
      auditId = "")

  val disposeResponseSoapEndpointFailure =
    DisposeResponse(success = false,
      message = "Fake Web Dispose Service - Bad response - Soap endpoint down",
      transactionId = "", // No transactionId because the soap endpoint is down
      registrationNumber = "",
      auditId = "",
      responseCode = Some("ms.dispose.response.endpointdown"))

  val disposeResponseWithResponseCode =
    DisposeResponse(success = false,
      message = "Fake Web Dispose Service - Bad response",
      transactionId = transactionIdValid, // We should always get back a transaction id even for failure scenarios. Only exception is if the soap endpoint is down
      registrationNumber = "",
      auditId = "",
      responseCode = Some("test response code"))

  val disposeResponseNoResponseCode =
    DisposeResponse(success = false,
      message = "Fake Web Dispose Service - Bad response - No response code",
      transactionId = "",
      registrationNumber = "",
      auditId = "",
      responseCode = None)

  val consentValid = "true"
  val mileageValid = "20000"
}