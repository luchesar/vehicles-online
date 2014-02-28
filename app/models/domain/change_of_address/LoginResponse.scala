package models.domain.change_of_address

import play.api.libs.json.Json


case class LoginResponse(success: Boolean, message: String, loginConfirmationModel: LoginConfirmationModel)

object LoginResponse {
  import models.domain.change_of_address.LoginConfirmationModel.loginConfirmationModelJson
  implicit val loginResponseJson = Json.format[LoginResponse]
}


