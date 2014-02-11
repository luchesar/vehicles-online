package mappings

import play.api.data.Mapping
import play.api.data.Forms._

object Postcode {
  val minLength = 5
  val maxLength = 8
  // val pattern = s"\\d{$minLength,$maxLength}" // Digits only with specified size.
  val key = "Postcode"

  def postcode (minLength: Int = mappings.Postcode.minLength, maxLength: Int = mappings.Postcode.maxLength): Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying constraints.Postcode.rules
  }
}
