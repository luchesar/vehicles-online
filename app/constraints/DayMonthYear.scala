package constraints

import play.api.data.validation._
import scala.util.{Failure, Success, Try}
import org.joda.time.DateTime
import play.api.data.validation.ValidationError

object DayMonthYear {
  def validDate: Constraint[models.DayMonthYear] = Constraint[models.DayMonthYear]("constraint.required") {
    case models.DayMonthYear(None, None, None, _, _) => Invalid(ValidationError("error.required"))
    case dmy@models.DayMonthYear(_, _, _, _, _) => dateValidation(dmy)
  }

  private def dateValidation(dmy: models.DayMonthYear): ValidationResult = Try(new DateTime(dmy.year.get, dmy.month.get, dmy.day.get, 0, 0)) match {
    case Success(dt: DateTime) if dt.getYear > 9999 || dt.getYear < 999 => Invalid(ValidationError("error.invalid"))
    case Success(dt: DateTime) if dt.getYear > 9999 || dt.getYear < 999 => Invalid(ValidationError("error.invalid"))
    case Success(dt: DateTime) => Valid
    case Failure(_) => Invalid(ValidationError("error.invalid"))
  }
}
