package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._

object RegistrationNumber {
  val minLength = 2
  val maxLength = 8
  val key = "RegistrationNumber"

  def registrationNumber (minLength: Int = Int.MinValue, maxLength: Int = Int.MaxValue): Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying constraints.RegistrationNumber.rules
  }
}
