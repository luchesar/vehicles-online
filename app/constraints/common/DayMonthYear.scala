package constraints.common

import play.api.data.validation._
import scala.util.{Success, Try}
import org.joda.time.DateTime
import play.api.data.validation.ValidationError
import services.DateService

object DayMonthYear {
  final val MinYear = 999
  final val MaxYear = 9999

  def required: Constraint[Int] = Constraint[Int]("constraint.required") {
    case i if i > 0 => Valid
    case _ => Invalid(ValidationError("error.dropDownInvalid"))
  }

  def validDate(minYear: Int = MinYear, maxYear: Int = MaxYear): Constraint[models.DayMonthYear] = {
    def dateValidation(dmy: models.DayMonthYear): ValidationResult = Try(new DateTime(dmy.year, dmy.month, dmy.day, 0, 0)) match {
      case Success(dt: DateTime) if dt.getYear > minYear && dt.getYear < maxYear => Valid
      case _ => Invalid(ValidationError("error.invalid"))
    }

    Constraint("constraint.required") {
      case dmy@models.DayMonthYear(_, _, _, _, _) => dateValidation(dmy)
      case _ => Invalid(ValidationError("error.required"))
    }
  }

  def after(earliest: models.DayMonthYear): Constraint[models.DayMonthYear] = {
    // Date must be after a year
    import scala.language.postfixOps
    Constraint("constraint.withinTwoYears") {
      case dmy@models.DayMonthYear(_, _, _, _, _) if dmy >= earliest => Valid
      case _ => Invalid(ValidationError("error.withinTwoYears"))
    }
  }

  def notInFuture(dateService: DateService): Constraint[models.DayMonthYear] = {
    // Date must be after a year
    import scala.language.postfixOps
    Constraint("constraint.notInFuture") {
      case dmy@models.DayMonthYear(_, _, _, _, _) if dmy <= dateService.today => Valid
      case _ => Invalid(ValidationError("error.notInFuture"))
    }
  }
}