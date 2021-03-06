package services.fakes

import models.DayMonthYear
import org.joda.time.{DateTime, Instant}
import services.DateService

final class FakeDateServiceImpl extends DateService {
  import FakeDateServiceImpl.{DateOfDisposalDayValid, DateOfDisposalMonthValid, DateOfDisposalYearValid}

  override def today = DayMonthYear(
    DateOfDisposalDayValid.toInt,
    DateOfDisposalMonthValid.toInt,
    DateOfDisposalYearValid.toInt
  )

  override def now = Instant.now()

  override def dateTimeISOChronology: String = new DateTime(
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