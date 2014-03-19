package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._
import constraints.common.DayMonthYear._

object DayMonthYear {
  val dateofBirthID = "dateOfBirth"

  val dayValid = "25"
  val monthValid = "11"
  val yearValid = "1968"
  val dayMonthYearValid = models.DayMonthYear(25, 11, 1968)

  val dayDateOfBirthID = "dateOfBirth.day"
  val monthDateOfBirthID = "dateOfBirth.month"
  val yearDateOfBirthID = "dateOfBirth.year"

  val dayMonthYear: Mapping[models.DayMonthYear] = mapping(
    "day" -> number(max = 100).verifying(required),
    "month" -> number(max = 100).verifying(required),
    "year" -> number(max = 99999).verifying(required),
    "hour" -> optional(number(max = 100, min = 0)),
    "minutes" -> optional(number(max = 100, min = 0)))(models.DayMonthYear.apply)(models.DayMonthYear.unapply)
}
