package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._
import models.domain.common.ValtechForms._

object MultiLineAddress {
  val id = "address"
  val lineOneId = "lineOne"
  val lineTwoId = "lineTwo"
  val lineThreeId = "lineThree"

  val address: Mapping[models.domain.common.MultiLineAddress] = mapping(
    lineOneId -> optional(carersText(maxLength = 35)),
    lineTwoId -> optional(carersText(maxLength = 35)),
    lineThreeId -> optional(carersText(maxLength = 35)))(models.domain.common.MultiLineAddress.apply)(models.domain.common.MultiLineAddress.unapply)
}
