package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._

object Mileage {
  val minLength = 1
  val maxLength = 6
  val pattern = s"\\d{$minLength,$maxLength}" // Digits only with specified size.
  val key = "Mileage"

  def mileage (min: Int = 0, max: Int = 999999): Mapping[Option[Int]] = {
    optional(number(min, max))
  }
}


