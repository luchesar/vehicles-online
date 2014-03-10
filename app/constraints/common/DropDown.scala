package constraints.common

import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}

object DropDown {
  def validDropDown(dropDownOptions: Map[String, String]): Constraint[String] = Constraint("constraint.validDropDown") { input =>
    dropDownOptions.contains(input) match {
      case true => Valid
      case false => Invalid(ValidationError("error.dropDownInvalid"))
    }
  }
}
