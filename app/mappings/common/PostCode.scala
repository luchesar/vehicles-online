package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._
import mappings.common

object PostCode {
  val minLength = 5
  val maxLength = 8
  val key = "postCode"
  val postcodeId = "postcode"

  def postcode (minLength: Int = common.PostCode.minLength, maxLength: Int = common.PostCode.maxLength): Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying constraints.Postcode.rules
  }
}
