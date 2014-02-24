package constraints

import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}
import models.domain.disposal_of_vehicle.AddressLinesModel
import mappings.common.AddressLines._
import models.domain.disposal_of_vehicle.AddressLinesModel

object AddressLines {
  def validAddressLines: Constraint[AddressLinesModel] = Constraint[AddressLinesModel]("constraint.required") { input =>
    input match {
      case AddressLinesModel(Some(line1), _, _, _) if(input.line1.isDefined && input.toViewFormat.length <= maxLengthOfLinesConcatenated) => Valid
      case _ => Invalid(ValidationError("error.address.line1Required"))
    }
  }
}
