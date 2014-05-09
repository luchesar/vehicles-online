package services.fakes

import models.DayMonthYear
import services.DateService

class FakeDateServiceImpl extends DateService {

  import FakeDateServiceImpl._

  override def today = DayMonthYear(dateOfDisposalDayValid.toInt, dateOfDisposalMonthValid.toInt, dateOfDisposalYearValid.toInt)
}

object FakeDateServiceImpl {
  val dateOfDisposalDayValid = "25"
  val dateOfDisposalMonthValid = "11"
  val dateOfDisposalYearValid = "1970"
}
