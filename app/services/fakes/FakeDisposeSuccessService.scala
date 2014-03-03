package services.fakes

import services.DisposeService
import models.domain.disposal_of_vehicle._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import models.domain.disposal_of_vehicle.DisposeResponse

class FakeDisposeSuccessService extends DisposeService {
  val successMessage = "Fake Web Dispose Service - Good response"
  val disposeResponseSuccess = DisposeResponse(success = true, message = successMessage, transactionId = "1234")
  val disposeResponseFails = DisposeResponse(success = false, message = successMessage, transactionId = "5678")

  override def invoke(cmd: DisposeModel): Future[DisposeResponse] = Future {
    disposeResponseSuccess
  }
}