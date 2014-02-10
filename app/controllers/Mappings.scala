package controllers

import play.api.data.validation._
import play.api.data.Mapping
import play.api.data.Forms._
import play.api.data.validation.ValidationError

object Mappings {


  object Authentication {
    val minLength = 6
    val maxLength = 6
    val pattern = s"\\d{$minLength,$maxLength}" // Digits only with specified size.     
  }
  
  object V5cReferenceNumber {
    val minLength = 11
    val maxLength = 11
    val pattern = s"\\d{$minLength,$maxLength}" // Digits only with specified size.
    val key = "V5cReferenceNumber"
  }

  object V5cRegistrationNumber {
    val maxLength = 7
    val key = "V5cRegistrationNumber"
  }

  object LoginConfirmationModel {
    val key = "LoginConfirmationModel"
  }
  
  object Name {
    val maxLength = 35
  }

  val fifty = 50

  val sixty = 60

  val two = 2

  val hundred = 100

  val yes = "yes"

  val no = "no"


  def validPhoneNumber: Constraint[String] = Constraint[String]("constraint.phoneNumber") { phoneNumber =>
    val phoneNumberPattern = """[0-9 \-]{1,20}""".r

    phoneNumberPattern.pattern.matcher(phoneNumber).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.invalid"))
    }
  }


  def validDecimalNumberRequired: Constraint[String] = Constraint[String]("constraint.decimal") { decimal =>
    val decimalPattern = """^[0-9]{1,12}(\.[0-9]{1,2})?$""".r

    if(decimal != null && !decimal.isEmpty()) {
      decimalPattern.pattern.matcher(decimal).matches match {
        case true => Valid
        case false => Invalid(ValidationError("decimal.invalid"))
      }
    } else {
       Valid
    }
  }

  def validDecimalNumber: Constraint[String] = Constraint[String]("constraint.decimal") { decimal =>
    val decimalPattern = """^[0-9]{1,12}(\.[0-9]{1,2})?$""".r

    decimalPattern.pattern.matcher(decimal).matches match {
      case true => Valid
      case false => Invalid(ValidationError("decimal.invalid"))
    }
  }

  def validNumber: Constraint[String] = Constraint[String]("constraint.number") { number =>
    val numberPattern = """^[0-9]*$""".r

    numberPattern.pattern.matcher(number).
      matches match {
      case true => Valid
      case false => Invalid(ValidationError("number.invalid"))
    }
  }

  def validYesNo: Constraint[String] = Constraint[String]("constraint.yesNo") { answer =>
    answer match {
      case `yes` => Valid
      case `no` => Valid
      case _ => Invalid(ValidationError("yesNo.invalid"))
    }
  }

  def validNationality: Constraint[String] = Constraint[String]("constraint.nationality") { nationality =>
    val nationalityPattern = """[a-zA-Z \-]{1,60}""".r

    nationalityPattern.pattern.matcher(nationality).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.nationality"))
    }
  }

  def simpleTextLine: Constraint[String] = Constraint[String]("constraint.simpleTextLine") { simpleTextLine =>
    val simpleTextLinePattern = """^[a-zA-Z0-9 ]*$""".r

    simpleTextLinePattern.pattern.matcher(simpleTextLine).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.address.characters"))
    }
  }

  def restrictedStringText: Constraint[String] = Constraint[String]("constraint.restrictedStringText") { restrictedString =>
    // This is the same allowable characters as per the xml schema with some characters removed
    // The removed characters are : £()@<>
    val restrictedStringPattern = """^[A-Za-z0-9\s~!"#$%&'\*\+,\-\./:;=\?\[\\\]_\{\}\^]*$""".r

    restrictedStringPattern.pattern.matcher(restrictedString).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.restricted.characters"))
    }
  }

  def PIN (minLength: Int = Int.MinValue, maxLength: Int = Int.MaxValue): Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying validNumberOnly
  }

  def validNumberOnly: Constraint[String] = Constraint[String]("constraint.restrictedvalidNumberOnly") { input =>
    val inputRegex = """^\d[0-9]*$""".r
    inputRegex.pattern.matcher(input).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.restricted.validNumberOnly"))
    }
  }

  def V5cReferenceNumber (minLength: Int = V5cReferenceNumber.minLength, maxLength: Int = V5cReferenceNumber.maxLength): Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying validNumberOnly
  }

  def V5CRegistrationNumber (minLength: Int = Int.MinValue, maxLength: Int = Int.MaxValue): Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying validVRN
  }

  def validVRN: Constraint[String] = Constraint[String]("constraint.restrictedvalidVRN") { input =>
    val inputRegex = """^[A-Za-z0-9 _]*$""".r
    inputRegex.pattern.matcher(input).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.restricted.validVRNOnly"))
    }
  }


  object Postcode {
    val minLength = 5
    val maxLength = 8
    // val pattern = s"\\d{$minLength,$maxLength}" // Digits only with specified size.
    val key = "Postcode"
  }

  def Postcode (minLength: Int = Int.MinValue, maxLength: Int = Int.MaxValue): Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying validPostcode
  }


  def validPostcode: Constraint[String] = Constraint[String]("constraint.restrictedvalidPostcode") { input =>
    val inputRegex = """^(?i)(GIR 0AA)|((([A-Z][0-9][0-9]?)|(([A-Z][A-HJ-Y][0-9][0-9]?)|(([A-Z][0-9][A-Z])|([A-Z][A-HJ-Y][0-9]?[A-Z]))))[ ]?[0-9][A-Z]{2})$""".r

    inputRegex.pattern.matcher(input).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.restricted.validPostcode"))
    }
  }

  object KeeperName {
    val minLength = 1
    val maxLength = 100
    val key = "KeeperName"
  }

  def consent: Mapping[Boolean] = {
    boolean verifying validConsent
  }

  def validConsent: Constraint[Boolean] = Constraint[Boolean]("constraint.validConsent") { input =>
    input match {
      case true => Valid
      case false => Invalid(ValidationError("disposal_dispose.consentnotgiven"))
    }
  }

  def dropDown(dropDownOptions: Map[String, String]): Mapping[String] = {
    nonEmptyText(maxLength = 12) verifying validDropDown(dropDownOptions)
  }

  def validDropDown(dropDownOptions: Map[String, String]): Constraint[String] = Constraint[String]("constraint.validDropDown") { input =>
    dropDownOptions.contains(input) match {
      case true => Valid
      case false => Invalid(ValidationError("error.dropDownInvalid"))
    }
  }
}