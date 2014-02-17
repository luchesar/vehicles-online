package constraints

import play.api.data.validation.{Valid, ValidationError, Invalid, Constraint}
import models.domain.common.MultiLineAddress

object MultilineAddress {
  def requiredAddress: Constraint[MultiLineAddress] = Constraint[MultiLineAddress]("constraint.required") { a =>
    if (a.lineOne.isEmpty) Invalid(ValidationError("error.required")) else Valid
  }
}
