package constraints

import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}

object V5CRegistrationNumber {
  def validVRN: Constraint[String] = Constraint[String]("constraint.restrictedvalidVRN") { input =>
    val inputRegex = """^[A-Za-z0-9 _]*$""".r
    inputRegex.pattern.matcher(input).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.restricted.validVRNOnly"))
    }
  }
}
