package constraints.disposal_of_vehicle

import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}

object RegistrationNumber {
  def validRegistrationNumber: Constraint[String] = Constraint("constraint.restrictedvalidVRN") { input =>
    val inputRegex = """^[A-Za-z0-9 _]*$""".r
    inputRegex.pattern.matcher(input).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.restricted.validVRNOnly"))
    }
  }
}
