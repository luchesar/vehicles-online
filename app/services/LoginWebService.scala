package services

import scala.concurrent.Future
import models.domain.change_of_address.LoginPageModel
import play.api.libs.ws.Response
import models.domain.change_of_address.LoginResponse

trait LoginWebService {
  protected def callWebService(cmd: LoginPageModel): Future[Response]

  def invoke(cmd: LoginPageModel): Future[LoginResponse]
}
