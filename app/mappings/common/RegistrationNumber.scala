package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._
import constraints.disposal_of_vehicle.RegistrationNumber.validRegistrationNumber

object RegistrationNumber {
  final val MinLength = 2
  final val MaxLength = 8
  final val Key = "RegistrationNumber"

  def registrationNumber: Mapping[String] = {
    nonEmptyText(MinLength, MaxLength) verifying validRegistrationNumber
  }
}
