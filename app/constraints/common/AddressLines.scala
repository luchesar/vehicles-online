package constraints.common

import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}
import mappings.common.AddressLines._
import models.domain.common.AddressLinesModel

object AddressLines {
  def validAddressLines: Constraint[AddressLinesModel] = Constraint[AddressLinesModel]("constraint.required") {
    case input@AddressLinesModel(line1, _, _, _)  => {
      if(input.totalCharacters <= maxLengthOfLinesConcatenated) Valid
      else Invalid(ValidationError("error.address.maxLengthOfLinesConcatenated"))
    }
    case _ => Invalid(ValidationError("error.address.line1Required"))
  }
}
