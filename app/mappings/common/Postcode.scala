package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._
import constraints.common.Postcode.validPostcode

object Postcode {
  final val PostcodeId = "postcode"
  final val Key = "postCode"
  private final val MinLength = 5
  final val MaxLength = 8

  def postcode: Mapping[String] = {
    nonEmptyText(MinLength, MaxLength) verifying validPostcode
  }
}
