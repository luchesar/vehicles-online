package mappings.common

import play.api.data.Mapping
import play.api.data.Forms.{optional, number}

object Uprn {
  final val UprnId = "uprn"

  def uprn: Mapping[Option[Int]] = optional(number)
}