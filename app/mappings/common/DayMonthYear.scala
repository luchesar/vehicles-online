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
    dayId -> number(max = 31).verifying(required), // TODO magic numbers
    monthId -> number(max = 12).verifying(required),
    yearId -> number.verifying(required),
    hourId -> optional(number(min = 0, max = 24)),
    minutesId -> optional(number(min = 0, max = 60)))(models.DayMonthYear.apply)(models.DayMonthYear.unapply)
}
