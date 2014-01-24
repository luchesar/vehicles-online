package models.domain.change_of_address

case class LoginConfirmationModel(firstName: String, surname: String, dob: String, address: Address)

case class Address(line1: String, postcode: String, country: String)