package constraints.common

import play.api.data.validation._
import scala.util.{Failure, Success, Try}
import org.joda.time.DateTime
import play.api.data.validation.ValidationError
import services.DateService

object DayMonthYear {
  def required: Constraint[Int] = Constraint[Int]("constraint.required") {
    case i if i > 0  => Valid
    case _ => Invalid(ValidationError("error.dropDownInvalid"))
  }

  def validDate: Constraint[models.DayMonthYear] = Constraint("constraint.required") {
    case dmy@models.DayMonthYear(_, _, _, _, _) => dateValidation(dmy)
    case _ => Invalid(ValidationError("error.required"))
  }

  private def dateValidation(dmy: models.DayMonthYear): ValidationResult = Try(new DateTime(dmy.year, dmy.month, dmy.day, 0, 0)) match {
    case Success(dt: DateTime) if dt.getYear > 9999 || dt.getYear < 999 => Invalid(ValidationError("error.invalid"))
    case Success(dt: DateTime) if dt.getYear > 9999 || dt.getYear < 999 => Invalid(ValidationError("error.invalid"))
    case Success(dt: DateTime) => Valid
    case Failure(_) => Invalid(ValidationError("error.invalid"))
  }

  def withinTwoYears(dateService: DateService): Constraint[models.DayMonthYear] = {
    import scala.language.postfixOps
    Constraint("constraint.withinTwoYears") {
      case dmy@models.DayMonthYear(_, _, _, _, _) if dmy.`yyyy-MM-dd` >= (dateService.today - 2 years).`yyyy-MM-dd` => Valid
      case _ => Invalid(ValidationError("error.withinTwoYears"))
    }
  }
}