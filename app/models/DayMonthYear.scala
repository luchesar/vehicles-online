package models

import scala.util.{Failure, Success, Try}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

case class DayMonthYear(day: Int, month: Int, year: Int,
                        hour: Option[Int] = None, minutes: Option[Int] = None) {
  def withTime(hour: Int, minutes: Int) = copy(hour = Some(hour), minutes = Some(minutes))

  def `yyyy-MM-dd`: String = format("yyyy-MM-dd")

  def `dd month, yyyy`: String = format("dd MMMM, yyyy")

  def `yyyy-MM-dd'T'HH:mm:00`: String = format("yyyy-MM-dd'T'HH:mm:00")

  def `dd/MM/yyyy`: String = pad(day) + "/" + pad(month) + "/" + year.toString

  def -(amount: Int) = new Period {
    override def days = adjust { _.minusDays(amount) }

    override def weeks = adjust { _.minusWeeks(amount) }

    override def months = adjust { _.minusMonths(amount) }

    override def years = adjust { _.minusYears(amount) }
  }

  def numberOfCharactersInput = List(Some(day), Some(month), Some(year), hour, minutes).foldLeft(0) { (x, i) => x + i.fold(0)(_.toString.length) }

  private def pad(i: Option[Int]): String = i.fold("")(i => if (i < 10) s"0$i" else s"$i")

  private def pad(i: Int): String = if (i < 10) s"0$i" else s"$i"

  private def adjust(f: DateTime => DateTime) = Try(new DateTime(year, month, day, 0, 0)) match {
    case Success(dt: DateTime) =>
      val newDateTime = f(dt)
      DayMonthYear(newDateTime.dayOfMonth().get, newDateTime.monthOfYear().get, newDateTime.year().get, hour, minutes)
    case Failure(_) => this
  }

  private def format(pattern: String): String = Try(new DateTime(year, month, day, hour.getOrElse(0), minutes.getOrElse(0))) match {
    case Success(dt: DateTime) => DateTimeFormat.forPattern(pattern).print(dt)
    case Failure(_) => ""
  }

  def toDateTime: Option[DateTime] = {
    try {
      Some(new DateTime(year, month, day, hour.getOrElse(0), minutes.getOrElse(0)))
    } catch {
      case e: Exception => None
    }
  }
}

object DayMonthYearComparator extends Ordering[Option[DayMonthYear]] {
  def compare(a: Option[DayMonthYear], b: Option[DayMonthYear]): Int = {
    (Try(new DateTime(a.get.year, a.get.month, a.get.day, a.get.hour.get, a.get.minutes.get)) match {
      case Success(dt: DateTime) => dt
      case Failure(_) => new DateTime()
    }).compareTo(
        Try(new DateTime(b.get.year, b.get.month, b.get.day, b.get.hour.get, b.get.minutes.get)) match {
          case Success(dt: DateTime) => dt
          case Failure(_) => new DateTime()
        })
  }
}

object DayMonthYear {
  import play.api.libs.json.Json

  implicit val dayMonthYearFormat = Json.format[DayMonthYear]

  def from(dt: DateTime) = DayMonthYear(dt.dayOfMonth().get, dt.monthOfYear().get, dt.year().get, Some(dt.hourOfDay().get()), Some(dt.minuteOfHour().get))

  def today = {
    val now = DateTime.now()
    new DayMonthYear(now.dayOfMonth().get, now.monthOfYear().get, now.year().get)
  }
}

sealed trait Period {
  def day: DayMonthYear = days

  def days: DayMonthYear

  def week: DayMonthYear = weeks

  def weeks: DayMonthYear

  def month: DayMonthYear = months

  def months: DayMonthYear

  def year: DayMonthYear = years

  def years: DayMonthYear
}
