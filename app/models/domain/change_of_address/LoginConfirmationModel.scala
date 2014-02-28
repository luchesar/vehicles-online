package models.domain.change_of_address

import models.domain.disposal_of_vehicle.AddressViewModel
import play.api.libs.json.Json

case class LoginConfirmationModel(firstName: String, surname: String, dob: String, address: AddressViewModel)

object LoginConfirmationModel {
  implicit val loginConfirmationModelJson = Json.format[LoginConfirmationModel]
}