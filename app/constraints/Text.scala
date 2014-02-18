package constraints

import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}

object Text {
  def restrictedStringText: Constraint[String] = Constraint[String]("constraint.restrictedStringText") { restrictedString =>
  // This is the same allowable characters as per the xml schema with some characters removed
  // The removed characters are : £()@<>
    val restrictedStringPattern = """^[A-Za-z0-9\s~!"#$%&'\*\+,\-\./:;=\?\[\\\]_\{\}\^]*$""".r

    restrictedStringPattern.pattern.matcher(restrictedString).matches match {
      case true => Valid
      case false => Invalid(ValidationError("error.restricted.characters"))
    }
  }


}
