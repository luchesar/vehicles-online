package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._
import models.domain.common.AddressLinesModel

object AddressLines {
  val addressLinesId = "addressLines"
  val line1Id = "line1"
  val line2Id = "line2"
  val line3Id = "line3"
  val line4Id = "line4"
  val line1MinLength = 1
  val lineMaxLength = 30
  val maxLengthOfLinesConcatenated = 120
  val addressLinesCacheKey = "addressLines"

  def addressLines: Mapping[AddressLinesModel] = mapping(
    line1Id -> nonEmptyText(minLength = line1MinLength, maxLength = lineMaxLength),
    line2Id -> optional(text(maxLength = lineMaxLength)),
    line3Id -> optional(text(maxLength = lineMaxLength)),
    line4Id -> optional(text(maxLength = lineMaxLength))
  )(AddressLinesModel.apply)(AddressLinesModel.unapply)
}
