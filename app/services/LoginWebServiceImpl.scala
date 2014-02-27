package services

import models.domain.change_of_address._
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.Logger
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import utils.helpers.Config
import models.domain.disposal_of_vehicle.{AddressViewModel, AddressAndPostcodeModel, AddressLinesModel}

class LoginWebServiceImpl() extends LoginWebService {
  implicit val writeLoginPage = Json.writes[LoginPageModel]
  implicit val addressLinesModel = Json.format[AddressLinesModel]
  implicit val addressViewModel = Json.format[AddressViewModel]
  implicit val addressAndPostcodeModel = Json.format[AddressAndPostcodeModel]
  implicit val loginConfirmationModel = Json.format[LoginConfirmationModel]
  implicit val loginResponse = Json.format[LoginResponse]

  override def invoke(cmd: LoginPageModel): Future[LoginResponse] = {
    val endPoint = s"${Config.microServiceBaseUrl}/login-page"
    Logger.debug(s"Calling Login micro service on ${endPoint}...")
    val futureOfResponse = WS.url(endPoint).post(Json.toJson(cmd))

    futureOfResponse.map {
      resp =>
        Logger.debug(s"Http response code from Login micro service was: ${resp.status}")
        resp.json.as[LoginResponse]
    }
  }
}

