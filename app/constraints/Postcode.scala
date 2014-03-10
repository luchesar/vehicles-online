package constraints

import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}

object Postcode {
  def validPostcode: Constraint[String] = Constraint("constraint.restrictedvalidPostcode") { input =>
    val inputRegex = """^(?i)(GIR 0AA)|((([A-Z][0-9][0-9]?)|(([A-Z][A-HJ-Y][0-9][0-9]?)|(([A-Z][0-9][A-Z])|([A-Z][A-HJ-Y][0-9]?[A-Z]))))[ ]?[0-9][A-Z]{2})$""".r

    inputRegex.pattern.matcher(input).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.restricted.validPostcode"))
    }
  }
}
