package mappings.change_of_address

import play.api.data.Mapping
import play.api.data.Forms._
import constraints.NumberOnly

object Pin {
  def pin (minLength: Int = Int.MinValue, maxLength: Int = Int.MaxValue): Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying NumberOnly.rules
  }
}
