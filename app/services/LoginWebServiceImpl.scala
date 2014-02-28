package services

import models.domain.change_of_address._
import play.api.libs.json.Json
import play.api.libs.ws.{Response, WS}
import play.api.Logger
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import utils.helpers.Config

class LoginWebServiceImpl() extends LoginWebService {

  override protected def callWebService(cmd: LoginPageModel): Future[Response] = {
    val endPoint = s"${Config.microServiceBaseUrl}/login-page"
    Logger.debug(s"Calling Login micro service on ${endPoint}...")
    WS.url(endPoint).post(Json.toJson(cmd))
  }

  override def invoke(cmd: LoginPageModel): Future[LoginResponse] = {
    val result = callWebService(cmd)

    result.map { resp =>
      Logger.debug(s"Http response code from Login micro service was: ${resp.status}")
      resp.json.as[LoginResponse]
    }
  }
}