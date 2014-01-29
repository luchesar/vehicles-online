package services

import models.domain.change_of_address._
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.Logger
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

case class LoginServiceImpl() extends LoginWebService {
  override def invoke(cmd: LoginPageModel): Future[LoginResponse] = {
    implicit val loginPage = Json.writes[LoginPageModel]
val endPoint = "http://localhost:8080/vehicles/login-page"
    val futureOfResponse = WS
      .url("http://localhost:8080/vehicles/login-page").post(Json.toJson(cmd))
    Logger.debug(s"Calling V5C micro service on ${endPoint}...")

    futureOfResponse.map{ resp =>
      implicit val address = Json.reads[Address]
      implicit val loginConfirmationModel = Json.reads[LoginConfirmationModel]
      implicit val loginResponse = Json.reads[LoginResponse]

      Logger.debug(s"Http response code from Login micro service was: ${resp.status}")

      resp.json.as[LoginResponse]
    }
  }
}

