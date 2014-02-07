package models.domain.common

case class Address(line1: String, line2: Option[String] = None, line3: Option[String] = None, line4: Option[String] = None, postCode: String)