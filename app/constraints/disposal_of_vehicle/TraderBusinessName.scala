package constraints.disposal_of_vehicle

import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}

object TraderBusinessName {
  def validTraderBusinessName: Constraint[String] = Constraint[String]("constraint.validTraderBusinessName") { restrictedString =>
    // This is the same allowable characters as per the xml schema with some characters removed
    // The removed characters are : £()@<>
    val restrictedStringPattern = """^[A-Za-z0-9\s\+\-\(\)\.\&\,\@]*$""".r

    restrictedStringPattern.pattern.matcher(restrictedString).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.validTraderBusinessName"))
    }
  }
}
