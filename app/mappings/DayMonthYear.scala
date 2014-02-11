package mappings

import play.api.data.Mapping
import play.api.data.Forms._

object DayMonthYear {
  val dayMonthYear: Mapping[models.DayMonthYear] = mapping(
    "day" -> optional(number(max = 100)),
    "month" -> optional(number(max = 100)),
    "year" -> optional(number(max = 99999)),
    "hour" -> optional(number(max = 100, min = 0)),
    "minutes" -> optional(number(max = 100, min = 0)))(models.DayMonthYear.apply)(models.DayMonthYear.unapply)
}
