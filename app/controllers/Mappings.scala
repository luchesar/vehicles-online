package controllers

import play.api.data.validation._
import play.api.data.Mapping
import play.api.data.Forms._
import play.api.data.validation.ValidationError
import models.DayMonthYear
import scala.util.{Failure, Success, Try}
import org.joda.time.DateTime


object Mappings {



  val fifty = 50

  val sixty = 60

  val two = 2

  val hundred = 100

  val yes = "yes"

  val no = "no"























  def dropDown(dropDownOptions: Map[String, String]): Mapping[String] = {
    nonEmptyText(maxLength = 12) verifying validDropDown(dropDownOptions)
  }

  def validDropDown(dropDownOptions: Map[String, String]): Constraint[String] = Constraint[String]("constraint.validDropDown") { input =>
    dropDownOptions.contains(input) match {
      case true => Valid
      case false => Invalid(ValidationError("error.dropDownInvalid"))
    }
  }

  object Mileage {
    val minLength = 1
    val maxLength = 6
    val pattern = s"\\d{$minLength,$maxLength}" // Digits only with specified size.
    val key = "Mileage"
  }

  def Mileage (minLength: Int = Int.MinValue, maxLength: Int = Int.MaxValue): Mapping[Option[Int]] = {
    optional(number(minLength, maxLength))
  }

  val dayMonthYear: Mapping[DayMonthYear] = mapping(
    "day" -> optional(number(max = 100)),
    "month" -> optional(number(max = 100)),
    "year" -> optional(number(max = 99999)),
    "hour" -> optional(number(max = 100, min = 0)),
    "minutes" -> optional(number(max = 100, min = 0)))(DayMonthYear.apply)(DayMonthYear.unapply)

  def validDate: Constraint[DayMonthYear] = Constraint[DayMonthYear]("constraint.required") {
    case DayMonthYear(None, None, None, _, _) => Invalid(ValidationError("error.required"))
    case dmy@DayMonthYear(_, _, _, _, _) => dateValidation(dmy)
  }

  private def dateValidation(dmy: DayMonthYear): ValidationResult = Try(new DateTime(dmy.year.get, dmy.month.get, dmy.day.get, 0, 0)) match {
    case Success(dt: DateTime) if dt.getYear > 9999 || dt.getYear < 999 => Invalid(ValidationError("error.invalid"))
    case Success(dt: DateTime) if dt.getYear > 9999 || dt.getYear < 999 => Invalid(ValidationError("error.invalid"))
    case Success(dt: DateTime) => Valid
    case Failure(_) => Invalid(ValidationError("error.invalid"))
  }
}