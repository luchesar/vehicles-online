package services.fakes

import services.DateService
import models.DayMonthYear

class FakeDateServiceImpl extends DateService {
  import FakeDateServiceImpl._
  override def today = DayMonthYear(dateOfDisposalDayValid.toInt, dateOfDisposalMonthValid.toInt, dateOfDisposalYearValid.toInt)
}

object FakeDateServiceImpl {
  val dateOfDisposalDayValid = "25"
  val dateOfDisposalMonthValid = "11"
  val dateOfDisposalYearValid = "1970"
}
