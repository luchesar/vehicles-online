package constraints

import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}

object NumberOnly {

  def rules: Constraint[String] = Constraint("constraint.restricted.validNumberOnly") { input =>
    val whitelist = """^\d[0-9]*$""".r
    if(whitelist.pattern.matcher(input).matches) Valid
    else Invalid(ValidationError("error.restricted.validNumberOnly"))
  }
}