package models.domain.change_of_address

import models.domain.disposal_of_vehicle.AddressAndPostcodeModel

case class LoginConfirmationModel(firstName: String, surname: String, dob: String, address: AddressAndPostcodeModel)
