package services.fakes

import services.DisposeService
import models.domain.disposal_of_vehicle._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import models.domain.disposal_of_vehicle.DisposeResponse
import play.api.Logger

class FakeDisposeService extends DisposeService {
  val successMessage = "Fake Web Dispose Service - Good response"
  val failMessage = "Fake Web Dispose Service - Bad response"
  val registrationNumberValid = "Q123ABC"
  val auditIdValid = "7575"

  def generateDisposeResponse(statusReturned: Boolean, messageReturned: String) =
    DisposeResponse(success = statusReturned, message = messageReturned, transactionId = "1234", registrationNumber = registrationNumberValid, auditId = auditIdValid)

  override def invoke(cmd: DisposeModel): Future[DisposeResponse] = Future {
    if (cmd.referenceNumber == FakeDisposeService.failureReferenceNumber) generateDisposeResponse(false, failMessage)
    else generateDisposeResponse(true, successMessage)
  }
}

object FakeDisposeService {
  val failureReferenceNumber = "9" * 11
}