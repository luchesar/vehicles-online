package models.domain.change_of_address

import play.api.libs.json.Json

case class LoginPageModel(username: String, password:String)

object LoginPageModel {
  implicit val loginPageModelJson = Json.format[LoginPageModel]
}