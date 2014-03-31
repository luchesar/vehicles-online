package constraints

import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}

object NumberOnly {
  def rules: Constraint[String] = Constraint("constraint.restrictedvalidNumberOnly") { input =>
    val whitelist = """^\d[0-9]*$""".r
    whitelist.pattern.matcher(input).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.restricted.validNumberOnly"))
    }
  }
}
