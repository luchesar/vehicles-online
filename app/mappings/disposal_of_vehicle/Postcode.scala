package mappings.disposal_of_vehicle

import play.api.data.Mapping
import play.api.data.Forms._

object Postcode {
  val postcodeID = "postcode"
  val minLength = 5
  val maxLength = 8

  def postcode (minLength: Int = minLength, maxLength: Int = maxLength): Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying constraints.Postcode.rules
  }
}
