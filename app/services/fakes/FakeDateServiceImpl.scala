package services.fakes

import models.DayMonthYear
import services.DateService

final class FakeDateServiceImpl extends DateService {
  import FakeDateServiceImpl._
  override def today = DayMonthYear(dateOfDisposalDayValid.toInt, dateOfDisposalMonthValid.toInt, dateOfDisposalYearValid.toInt)
}

object FakeDateServiceImpl {
  final val dateOfDisposalDayValid = "25"
  final val dateOfDisposalMonthValid = "11"
  final val dateOfDisposalYearValid = "1970"
}
