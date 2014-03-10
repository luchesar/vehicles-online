package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._
import constraints.Postcode.validPostcode

object Postcode {
  val postcodeId = "postcode"
  val key = "postCode"
  val minLength = 5
  val maxLength = 8

  def postcode (minLength: Int = minLength, maxLength: Int = maxLength): Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying validPostcode
  }
}
