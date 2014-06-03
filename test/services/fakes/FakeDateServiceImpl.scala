package services.fakes

import FakeDateServiceImpl._
import models.DayMonthYear
import services.DateService

final class FakeDateServiceImpl extends DateService {
  override def today = DayMonthYear(DateOfDisposalDayValid.toInt, DateOfDisposalMonthValid.toInt, DateOfDisposalYearValid.toInt)
}

object FakeDateServiceImpl {
  final val DateOfDisposalDayValid = "25"
  final val DateOfDisposalMonthValid = "11"
  final val DateOfDisposalYearValid = "1970"
}
