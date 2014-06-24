package constraints.common

import mappings.common.AddressLines._
import models.domain.common.AddressLinesModel
import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}

object AddressLines {

  def validAddressLines: Constraint[AddressLinesModel] = Constraint[AddressLinesModel]("constraint.required") {
    case input@AddressLinesModel(buildingNameOrNumber, _, _, _) =>
      // Regex states string must contain at least one number or letter, can also include punctuation.
      val format = """^(?=.*[a-zA-Z0-9])[A-Za-z0-9\s\-\,\.\/\\]*$""".r
      if (input.totalCharacters > MaxLengthOfLinesConcatenated)
        Invalid(ValidationError("error.address.maxLengthOfLinesConcatenated"))
      else if (!format.pattern.matcher(input.toViewFormat.mkString).matches)
        Invalid(ValidationError("error.address.characterInvalid"))
      else Valid
    case _ => Invalid(ValidationError("error.address.buildingNameOrNumber.invalid")) // TODO test coverage
  }

}