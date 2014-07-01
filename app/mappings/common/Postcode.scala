package mappings.common

import play.api.data.Mapping
import constraints.common.Postcode.validPostcode
import utils.helpers.FormExtensions._

object Postcode {
  final val PostcodeId = "postcode"
  final val Key = "postCode"
  private final val MinLength = 5
  final val MaxLength = 8

  def postcode: Mapping[String] = {
    nonEmptyTextWithTransform(_.toUpperCase.trim)(MinLength, MaxLength) verifying validPostcode
  }

}
