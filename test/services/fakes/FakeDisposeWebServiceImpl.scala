package services.fakes

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import FakeDisposeWebServiceImpl._
import FakeVehicleLookupWebService._
import models.domain.disposal_of_vehicle._
import play.api.Logger
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws.Response
import services.dispose_service.DisposeWebService

final class FakeDisposeWebServiceImpl extends DisposeWebService {
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
  final val transactionIdValid = "1234"
  private final val auditIdValid = "7575"
  private final val simulateMicroServiceUnavailable = "8" * 11
  private final val simulateSoapEndpointFailure = "9" * 11

  val disposeResponseSuccess =
    DisposeResponse(transactionId = transactionIdValid,
      registrationNumber = registrationNumberValid,
      auditId = auditIdValid)

  val disposeResponseSoapEndpointFailure =
    DisposeResponse(transactionId = "", // No transactionId because the soap endpoint is down
      registrationNumber = "",
      auditId = "",
      responseCode = None)

  val disposeResponseFailureWithResponseCode =
    DisposeResponse(transactionId = transactionIdValid, // We should always get back a transaction id even for failure scenarios. Only exception is if the soap endpoint is down
      registrationNumber = "",
      auditId = "",
      responseCode = Some("ms.vehiclesService.response.unableToProcessApplication"))

  val disposeResponseSoapEndpointTimeout =
    DisposeResponse(transactionId = "", // No transactionId because the soap endpoint is down
      registrationNumber = "",
      auditId = "",
      responseCode = None)

  val disposeResponseApplicationBeingProcessed =
    DisposeResponse(transactionId = transactionIdValid,
      registrationNumber = registrationNumberValid,
      auditId = auditIdValid,
      responseCode = None)

  val disposeResponseUnableToProcessApplication =
    DisposeResponse(transactionId = "", // No transactionId because the soap endpoint is down
      registrationNumber = "",
      auditId = "",
      responseCode = Some("ms.vehiclesService.response.unableToProcessApplication"))

  val disposeResponseUndefinedError =
    DisposeResponse(transactionId = "", // No transactionId because the soap endpoint is down
      registrationNumber = "",
      auditId = "",
      responseCode = Some("undefined"))


  final val consentValid = "true"
  final val mileageValid = "20000"
}