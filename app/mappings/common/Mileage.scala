package mappings.common

import play.api.data.Mapping
import play.api.data.Forms.{optional, number}

object Mileage {
  private final val minLength = 1
  final val maxLength = 6
  final val Max = 999999 // confirmed as max size by BAs
  final val Pattern = s"\\d{$minLength,$maxLength}" // Digits only with specified size.

  def mileage (max: Int = Max): Mapping[Option[Int]] = optional(number(max = max))
}


