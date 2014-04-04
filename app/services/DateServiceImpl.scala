package services

import models.DayMonthYear

class DateServiceImpl extends DateService{
  override def today = DayMonthYear.today
}
