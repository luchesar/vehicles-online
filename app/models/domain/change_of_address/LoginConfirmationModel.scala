package models.domain.change_of_address

import models.domain.common.Address

case class LoginConfirmationModel(firstName: String, surname: String, dob: String, address: Address)
