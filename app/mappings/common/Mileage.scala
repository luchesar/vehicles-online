package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._

object Mileage {
  val minLength = 1
  val maxLength = 6
  val max = 999999 // TODO check with BAs what the maximum milage is.
  val pattern = s"\\d{$minLength,$maxLength}" // Digits only with specified size.
  val key = "Mileage"

  def mileage (max: Int = max): Mapping[Option[Int]] = {
    optional(number(max = max))
  }
}


