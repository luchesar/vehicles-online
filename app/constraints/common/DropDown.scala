package constraints.common

import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}

object DropDown {
  def validDropDown(dropDownOptions: Map[String, String]): Constraint[String] = Constraint("constraint.validDropDown") { input =>
    if(dropDownOptions.contains(input)) Valid
    else Invalid(ValidationError("error.dropDownInvalid"))
  }
}
