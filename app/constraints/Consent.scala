package constraints

import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}

object Consent {
  def rules: Constraint[Boolean] = Constraint("constraint.validConsent") { input =>
    input match {
      case true => Valid
      case false => Invalid(ValidationError("disposal_dispose.consentnotgiven"))
    }
  }
}
