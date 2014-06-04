package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._
import models.domain.common.AddressLinesModel

object AddressLines {
  final val AddressLinesId = "addressLines"
  final val AddressOrBuildingNumberId = "line1"
  final val Line2Id = "line2"
  final val Line3Id = "line3"
  final val Line4Id = "line4"
  final val AddressOrBuildingNumberMinLength = 4
  final val Line4MinLength = 3
  final val LineMaxLength = 30
  final val MaxLengthOfLinesConcatenated = 120

  final val AddressLinesCacheKey = "addressLines"
  final val AddressLine1Holder = "No building name/num supplied"

  final val Line1 = 0
  final val Line2 = 1
  final val Line3 = 2
  final val emptyLine = ""

  def addressLines: Mapping[AddressLinesModel] = mapping(
    AddressOrBuildingNumberId -> nonEmptyText(minLength = AddressOrBuildingNumberMinLength, maxLength = LineMaxLength),
    Line2Id -> optional(text(maxLength = LineMaxLength)),
    Line3Id -> optional(text(maxLength = LineMaxLength)),
    Line4Id -> nonEmptyText(minLength = Line4MinLength, maxLength = LineMaxLength)
  )(AddressLinesModel.apply)(AddressLinesModel.unapply)
}
