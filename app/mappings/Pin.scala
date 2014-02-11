package mappings

import play.api.data.Mapping
import play.api.data.Forms._
import constraints.NumberOnly._

object Pin {
  def pin (minLength: Int = Int.MinValue, maxLength: Int = Int.MaxValue): Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying validNumberOnly
  }
}
