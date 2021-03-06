package services

import models.DayMonthYear
import org.joda.time.DateTime
import org.joda.time.Instant

final class DateServiceImpl extends DateService {
  override def today = DayMonthYear.today
  override def now = Instant.now()
  override def dateTimeISOChronology: String = DateTime.now().toString
}