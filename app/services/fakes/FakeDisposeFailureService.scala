package services.fakes

import services.DisposeService
import models.domain.disposal_of_vehicle._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import models.domain.disposal_of_vehicle.DisposeResponse

class FakeDisposeFailureService extends DisposeService {
  val failMessage = "Fake Web Dispose Service - Bad response"

  override def invoke(cmd: DisposeModel): Future[DisposeResponse] = Future {
    DisposeResponse(false, message = failMessage, transactionId = "1234")
  }
}