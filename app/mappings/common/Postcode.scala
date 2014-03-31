package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._
import constraints.common.Postcode.validPostcode

object Postcode {
  val postcodeId = "postcode"
  val key = "postCode"
  val minLength = 5
  val maxLength = 8

  def postcode: Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying validPostcode
  }
}
