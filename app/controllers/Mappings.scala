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
}