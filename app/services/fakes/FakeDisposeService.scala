package services.fakes

import models.domain.disposal_of_vehicle._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import models.domain.disposal_of_vehicle.DisposeResponse
import services.dispose_service.{DisposeWebService, DisposeService}
import play.api.libs.ws.Response
import scala.Some
import play.api.libs.json.Json

class FakeDisposeService extends DisposeService {
  val successMessage = "Fake Web Dispose Service - Good response"
  val failMessage = "Fake Web Dispose Service - Bad response"
  val registrationNumberValid = "Q123ABC"
  val auditIdValid = "7575"

  def generateDisposeResponse(statusReturned: Boolean, messageReturned: String) =
    DisposeResponse(success = statusReturned, message = messageReturned, transactionId = "1234", registrationNumber = registrationNumberValid, auditId = auditIdValid)

  override def invoke(cmd: DisposeRequest): Future[DisposeResponse] = Future {
    if (cmd.referenceNumber == FakeDisposeService.failureReferenceNumber) generateDisposeResponse(false, failMessage)
    else generateDisposeResponse(true, successMessage)
  }
}

object FakeDisposeService {
  val failureReferenceNumber = "9" * 11
}


class FakeDisposeWebServiceImpl extends DisposeWebService {
  override def callDisposeService(request: DisposeRequest): Future[Response] = Future {
    val disposeResponse =
      DisposeResponse(success = true,
        message = "Fake Web Dispose Service - Good response",
        transactionId = "1234",
        registrationNumber = "AB12AWR",
        auditId = "7575")
    val responseAsJson = Json.toJson(disposeResponse)

    new FakeResponse(status = 200, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
  }
}