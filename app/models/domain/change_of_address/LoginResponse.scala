package models.domain.change_of_address

import play.api.libs.json.Json


case class LoginResponse(success: Boolean, message: String, loginConfirmationModel: LoginConfirmationModel)

object LoginResponse {
  implicit val loginResponseJson = Json.format[LoginResponse]
}


