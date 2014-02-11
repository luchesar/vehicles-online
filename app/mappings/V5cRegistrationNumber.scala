package mappings

import play.api.data.Mapping
import play.api.data.Forms._
import constraints.V5CRegistrationNumber._

object V5cRegistrationNumber {
  val minLength = 2
  val maxLength = 8
  val key = "V5cRegistrationNumber"

  def v5CRegistrationNumber (minLength: Int = Int.MinValue, maxLength: Int = Int.MaxValue): Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying validVRN
  }
}
