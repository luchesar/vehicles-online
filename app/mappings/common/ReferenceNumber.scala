package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._
import constraints.NumberOnly
import mappings.common

object ReferenceNumber {
  val minLength = 11
  val maxLength = 11
  val pattern = s"\\d{$minLength,$maxLength}" // Digits only with specified size.
  val key = "ReferenceNumber"

  def referenceNumber (minLength: Int = ReferenceNumber.minLength, maxLength: Int = common.ReferenceNumber.maxLength): Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying NumberOnly.rules
  }
}