package constraints.common

import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}
import mappings.common.AddressLines._
import models.domain.common.AddressLinesModel

object AddressLines {
  def validAddressLines: Constraint[AddressLinesModel] = Constraint[AddressLinesModel]("constraint.required") {
    case input@AddressLinesModel(line1, _, _, _) => {
      // Reject when: Too many characters, HTML or XML markup
      val inputRegex = """^[A-Za-z0-9\s\-\,\.]*$""".r
      if (input.totalCharacters >= maxLengthOfLinesConcatenated) Invalid(ValidationError("error.address.maxLengthOfLinesConcatenated"))
      else if (!inputRegex.pattern.matcher(input.toViewFormat.mkString).matches) Invalid(ValidationError("error.address.characterinvalid"))
      else Valid
    }
    case _ => Invalid(ValidationError("error.address.line1Required"))
  }
}