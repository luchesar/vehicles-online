package mappings

import play.api.data.Mapping
import play.api.data.Forms._
import constraints.NumberOnly._

object V5cReferenceNumber {
  val minLength = 11
  val maxLength = 11
  val pattern = s"\\d{$minLength,$maxLength}" // Digits only with specified size.
  val key = "V5cReferenceNumber"

  def v5cReferenceNumber (minLength: Int = mappings.V5cReferenceNumber.minLength, maxLength: Int = mappings.V5cReferenceNumber.maxLength): Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying validNumberOnly
  }
}