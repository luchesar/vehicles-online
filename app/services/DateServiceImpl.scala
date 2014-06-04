package services

import models.DayMonthYear

final class DateServiceImpl extends DateService {
  override def today = DayMonthYear.today
  override def dateTimeISOChronology: String = org.joda.time.DateTime.now().toString
}
