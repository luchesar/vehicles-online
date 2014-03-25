package constraints.disposal_of_vehicle

import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}

object TraderBusinessName {
  def validTraderBusinessName: Constraint[String] = Constraint[String]("constraint.validTraderBusinessName") { restrictedString =>
    val restrictedStringPattern = """^(?=.*[a-zA-Z0-9])[A-Za-z0-9\s\+\-\(\)\.\&\,\@\']*$""".r

    restrictedStringPattern.pattern.matcher(restrictedString).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.validTraderBusinessName"))
    }
  }
}
