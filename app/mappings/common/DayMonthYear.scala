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
  val maxDaysInMonth = 31
  val maxMonthsInYear = 12
  val maxHoursInDay = 24
  val maxMinutesInHour = 59

  val dayMonthYear: Mapping[models.DayMonthYear] = mapping(
    dayId -> number(max = maxDaysInMonth).verifying(required),
    monthId -> number(max = maxMonthsInYear).verifying(required),
    yearId -> number.verifying(required),
    hourId -> optional(number(min = 0, max = maxHoursInDay)),
    minutesId -> optional(number(min = 0, max = maxMinutesInHour)))(models.DayMonthYear.apply)(models.DayMonthYear.unapply)
}
