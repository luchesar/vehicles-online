package mappings.common

import play.api.data.Forms.{number, optional}
import play.api.data.Mapping

object Uprn {
  final val UprnId = "uprn"

  def uprn: Mapping[Option[Int]] = optional(number)
}