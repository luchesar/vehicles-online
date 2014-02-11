package constraints

import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}

object Constraints {
  def validNumberOnly: Constraint[String] = Constraint[String]("constraint.restrictedvalidNumberOnly") { input =>
    val inputRegex = """^\d[0-9]*$""".r
    inputRegex.pattern.matcher(input).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.restricted.validNumberOnly"))
    }
  }
}
