package constraints.common

import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}
import mappings.common.AddressLines._
import models.domain.common.AddressLinesModel

object AddressLines {
  def validAddressLines: Constraint[AddressLinesModel] = Constraint[AddressLinesModel]("constraint.required") {
    case input@AddressLinesModel(line1, _, _, _)  => {
      // Reject when:
      // Too many characters
      // HTML or XML markup
      if(input.totalCharacters <= maxLengthOfLinesConcatenated && input.toViewFormat.forall(line => !line.contains("<"))) Valid // TODO: replace the line.contains with regex to check there are no HTML or XML markup in each string.
      else Invalid(ValidationError("error.address.maxLengthOfLinesConcatenated"))
    }
    case _ => Invalid(ValidationError("error.address.line1Required"))
  }
}