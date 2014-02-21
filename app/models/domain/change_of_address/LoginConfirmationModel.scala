package models.domain.change_of_address

import models.domain.disposal_of_vehicle.AddressViewModel

case class LoginConfirmationModel(firstName: String, surname: String, dob: String, address: AddressViewModel)
