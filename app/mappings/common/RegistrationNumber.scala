package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._
import constraints.disposal_of_vehicle.RegistrationNumber.rules

object RegistrationNumber {
  val minLength = 2
  val maxLength = 8
  val key = "RegistrationNumber"

  def registrationNumber (minLength: Int = Int.MinValue, maxLength: Int = Int.MaxValue): Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying rules
  }
}
