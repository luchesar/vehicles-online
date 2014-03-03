package services.fakes

import services.DisposeService
import models.domain.disposal_of_vehicle._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import models.domain.disposal_of_vehicle.DisposeResponse
import play.api.Logger

class FakeDisposeService extends DisposeService {
  val successMessage = "Fake Web Dispose Service - Good response"
  val disposeResponseSuccess = DisposeResponse(success = true, message = successMessage)
  val disposeResponseFails = DisposeResponse(success = false, message = successMessage)

  override def invoke(cmd: DisposeModel): Future[DisposeResponse] = Future {
    Logger.debug("****************** " + cmd.v5cKeeperName)
    if (cmd.v5cKeeperName == "fail") disposeResponseFails
    else disposeResponseSuccess
  }
}