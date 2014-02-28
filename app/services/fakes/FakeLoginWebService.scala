package services.fakes

import services.LoginWebService
import models.domain.disposal_of_vehicle.AddressViewModel
import models.domain.change_of_address.{LoginConfirmationModel, LoginResponse, LoginPageModel}
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import play.api.libs.ws.Response

/**
 * Fake implementation of the LoginWebService trait with in memory data
 */
class FakeLoginWebService extends LoginWebService {
  val address1 = AddressViewModel(address = Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))

  override def callWebService(cmd: LoginPageModel): Future[Response] = ???

  override def invoke(cmd: LoginPageModel): Future[LoginResponse] = Future {
    LoginResponse(success = true, "All ok ", LoginConfirmationModel("Roger", "Booth", "21/05/1977", address1))
  }
}
