package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._

object Uprn {
  val id = "uprn"
  val maxLength = 12

  def uprn: Mapping[Option[Int]] = {
    optional(number)
  }
}