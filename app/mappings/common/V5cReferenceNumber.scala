package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._
import constraints.NumberOnly
import mappings.common

object V5cReferenceNumber {
  val minLength = 11
  val maxLength = 11
  val pattern = s"\\d{$minLength,$maxLength}" // Digits only with specified size.
  val key = "V5cReferenceNumber"

  def v5cReferenceNumber (minLength: Int = V5cReferenceNumber.minLength, maxLength: Int = common.V5cReferenceNumber.maxLength): Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying NumberOnly.rules
  }
}