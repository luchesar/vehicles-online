package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._
import constraints.NumberOnly

object ReferenceNumber {
  final val MinLength = 11
  final val MaxLength = 11
  final val Pattern = s"\\d{$MinLength,$MaxLength}" // Digits only with specified size.
  final val Key = "ReferenceNumber"

  def referenceNumber: Mapping[String] = {
    nonEmptyText(MinLength, MaxLength) verifying NumberOnly.rules
  }
}