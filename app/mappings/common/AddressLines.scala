package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._
import play.api.data.validation.Invalid
import models.domain.disposal_of_vehicle.AddressLinesModel

object AddressLines {
  val id = "addressLines"
  val line1Id = "line1"
  val line2Id = "line2"
  val line3Id = "line3"
  val line4Id = "line4"
  val line1MinLength = 1
  val lineMaxLength = 75
  val maxLengthOfLinesConcatenated = 130

  def addressLines: Mapping[AddressLinesModel] = mapping(
    line1Id -> optional(text(minLength = line1MinLength, maxLength = lineMaxLength)),
    line2Id -> optional(text(maxLength = lineMaxLength)),
    line3Id -> optional(text(maxLength = lineMaxLength)),
    line4Id -> optional(text(maxLength = lineMaxLength))
  )(AddressLinesModel.apply)(AddressLinesModel.unapply)


}
