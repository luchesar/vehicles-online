package services.fakes

import FakeDateServiceImpl._
import models.DayMonthYear
import services.DateService

final class FakeDateServiceImpl extends DateService {
  override def today = DayMonthYear(DateOfDisposalDayValid.toInt, DateOfDisposalMonthValid.toInt, DateOfDisposalYearValid.toInt)

  override def dateTimeISOChronology: String = new org.joda.time.DateTime(
    DateOfDisposalYearValid.toInt,
    DateOfDisposalMonthValid.toInt,
    DateOfDisposalDayValid.toInt,
    0,
    0).toString
}

object FakeDateServiceImpl {
  final val DateOfDisposalDayValid = "25"
  final val DateOfDisposalMonthValid = "11"
  final val DateOfDisposalYearValid = "1970"
}
