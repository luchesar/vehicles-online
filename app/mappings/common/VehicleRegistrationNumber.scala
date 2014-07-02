package mappings.common

import play.api.data.Mapping
import play.api.data.Forms.nonEmptyText
import constraints.disposal_of_vehicle.RegistrationNumber.validRegistrationNumber

object VehicleRegistrationNumber {
  final val MinLength = 2
  final val MaxLength = 8

  def registrationNumber: Mapping[String] = nonEmptyText(MinLength, MaxLength) verifying validRegistrationNumber
}