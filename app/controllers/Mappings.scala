package controllers

import play.api.data.validation._
import play.api.data.validation.ValidationError

object Mappings {
  object Name {
    val maxLength = 35
  }

  val fifty = 50

  val sixty = 60

  val two = 2

  val hundred = 100

  val yes = "yes"

  val no = "no"

  def validPostcode: Constraint[String] = Constraint[String]("constraint.postcode") { postcode =>
    val postcodePattern = """^(?i)(GIR 0AA)|((([A-Z][0-9][0-9]?)|(([A-Z][A-HJ-Y][0-9][0-9]?)|(([A-Z][0-9][A-Z])|([A-Z][A-HJ-Y][0-9]?[A-Z]))))[ ]?[0-9][A-Z]{2})$""".r

    postcodePattern.pattern.matcher(postcode).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.postcode"))
    }
  }

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

}