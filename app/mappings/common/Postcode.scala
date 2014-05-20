package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._
import constraints.common.Postcode.validPostcode

object Postcode {
  final val PostcodeId = "postcode"
  final val Key = "postCode"
  private final val minLength = 5
  final val maxLength = 8

  def postcode: Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying validPostcode
  }
}
