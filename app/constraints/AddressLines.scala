package constraints

import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}
import models.domain.disposal_of_vehicle.AddressLinesModel
import mappings.common.AddressLines._
import models.domain.disposal_of_vehicle.AddressLinesModel

object AddressLines {
  def validAddressLines: Constraint[AddressLinesModel] = Constraint[AddressLinesModel]("constraint.required") {
    case input@AddressLinesModel(Some(line1), _, _, _)  => {
      if(input.totalCharacters <= maxLengthOfLinesConcatenated) Valid
      else Invalid(ValidationError("error.address.maxLengthOfLinesConcatenated"))
    }
    case _ => Invalid(ValidationError("error.address.line1Required"))
  }
}
