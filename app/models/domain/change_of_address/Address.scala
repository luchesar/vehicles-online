package models.domain.change_of_address

case class Address(line1: String, line2: Option[String], line3: Option[String], line4: Option[String], postCode: String)