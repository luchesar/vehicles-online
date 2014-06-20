package constraints.common

import org.joda.time.DateTime
import play.api.data.validation.{ValidationError, _}
import services.DateService

import scala.util.{Success, Try}

object DayMonthYear {
  private final val MinYear = 999
  private final val MaxYear = 9999

  def required: Constraint[Int] = Constraint[Int]("constraint.required") {
    case i if i > 0 => Valid
    case _ => Invalid(ValidationError("error.dropDownInvalid")) // TODO test coverage
  }

  def validDate(minYear: Int = MinYear, maxYear: Int = MaxYear): Constraint[models.DayMonthYear] = {
    def dateValidation(dmy: models.DayMonthYear): ValidationResult = Try(new DateTime(dmy.year, dmy.month, dmy.day, 0, 0)) match {
      case Success(dt: DateTime) if dt.getYear > minYear && dt.getYear < maxYear => Valid
      case _ => Invalid(ValidationError("error.invalid"))
    }

    Constraint("constraint.required") {
      case dmy: models.DayMonthYear => dateValidation(dmy)
      case _ => Invalid(ValidationError("error.required")) // TODO test coverage
    }
  }

  def after(earliest: models.DayMonthYear): Constraint[models.DayMonthYear] = {
    Constraint("constraint.withinTwoYears") {
      case dmy: models.DayMonthYear if dmy >= earliest => Valid
      case _ => Invalid(ValidationError("error.withinTwoYears"))
    }
  }

  def notInFuture(dateService: DateService): Constraint[models.DayMonthYear] = {
    Constraint("constraint.notInFuture") {
      case dmy: models.DayMonthYear if dmy <= dateService.today => Valid
      case _ => Invalid(ValidationError("error.notInFuture"))
    }
  }
}