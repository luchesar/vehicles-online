package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._
import constraints.common.DayMonthYear._

object DayMonthYear {
  val dayId = "day"
  val monthId = "month"
  val yearId = "year"
  val hourId = "hour"
  val minutesId = "minutes"

  val dayMonthYear: Mapping[models.DayMonthYear] = mapping(
    dayId -> number(max = 100).verifying(required),
    monthId -> number(max = 100).verifying(required),
    yearId -> number(max = 99999).verifying(required),
    hourId -> optional(number(max = 100, min = 0)),
    minutesId -> optional(number(max = 100, min = 0)))(models.DayMonthYear.apply)(models.DayMonthYear.unapply)
}
