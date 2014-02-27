package services.fakes

import services.{DisposeService}
import models.domain.disposal_of_vehicle._
import models.domain.change_of_address.LoginPageModel
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import models.domain.disposal_of_vehicle.DisposeResponse

class FakeDisposeService extends DisposeService {
  val successMessage = "Fake Web Dispose Service - Good response"

  override def invoke(cmd: DisposeModel): Future[DisposeResponse] = Future {
    DisposeResponse(true, message = successMessage)
  }
}
