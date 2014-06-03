package services.fakes

import FakeDisposeWebServiceImpl._
import FakeVehicleLookupWebService._
import models.domain.disposal_of_vehicle._
import play.api.Logger
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.libs.ws.Response
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import services.dispose_service.DisposeWebService

final class FakeDisposeWebServiceImpl extends DisposeWebService {
  override def callDisposeService(request: DisposeRequest): Future[Response] = Future {
    val disposeResponse: DisposeResponse = {
      request.referenceNumber match {
        case SimulateMicroServiceUnavailable => throw new RuntimeException("simulateMicroServiceUnavailable")
        case SimulateSoapEndpointFailure => disposeResponseSoapEndpointFailure
        case _ => disposeResponseSuccess
      }
    }
    val responseAsJson = Json.toJson(disposeResponse)
    Logger.debug(s"FakeVehicleLookupWebService callVehicleLookupService with: $responseAsJson")
    new FakeResponse(status = OK, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
  }
}

object FakeDisposeWebServiceImpl {
  final val TransactionIdValid = "1234"
  private final val AuditIdValid = "7575"
  private final val SimulateMicroServiceUnavailable = "8" * 11
  private final val SimulateSoapEndpointFailure = "9" * 11

  val disposeResponseSuccess =
    DisposeResponse(transactionId = TransactionIdValid,
      registrationNumber = RegistrationNumberValid,
      auditId = AuditIdValid)

  val disposeResponseSoapEndpointFailure =
    DisposeResponse(transactionId = "", // No transactionId because the soap endpoint is down
      registrationNumber = "",
      auditId = "",
      responseCode = None)

  val disposeResponseFailureWithResponseCode =
    DisposeResponse(transactionId = TransactionIdValid, // We should always get back a transaction id even for failure scenarios. Only exception is if the soap endpoint is down
      registrationNumber = "",
      auditId = "",
      responseCode = Some("ms.vehiclesService.response.unableToProcessApplication"))

  val disposeResponseFailureWithDuplicateDisposal =
    DisposeResponse(transactionId = TransactionIdValid, // We should always get back a transaction id even for failure scenarios. Only exception is if the soap endpoint is down
      registrationNumber = "",
      auditId = "",
      responseCode = Some("ms.vehiclesService.response.duplicateDisposalToTrade"))

  val disposeResponseSoapEndpointTimeout =
    DisposeResponse(transactionId = "", // No transactionId because the soap endpoint is down
      registrationNumber = "",
      auditId = "",
      responseCode = None)

  val disposeResponseApplicationBeingProcessed =
    DisposeResponse(transactionId = TransactionIdValid,
      registrationNumber = RegistrationNumberValid,
      auditId = AuditIdValid,
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


  final val ConsentValid = "true"
  final val MileageValid = "20000"
}