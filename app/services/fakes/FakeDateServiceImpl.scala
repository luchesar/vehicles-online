package services.fakes

import services.DateService
import models.DayMonthYear

class FakeDateServiceImpl extends DateService {
  override def today = {
    val dateOfDisposalDayValid = "25"
    val dateOfDisposalMonthValid = "11"
    val dateOfDisposalYearValid = "1970"
    DayMonthYear(dateOfDisposalDayValid.toInt, dateOfDisposalMonthValid.toInt, dateOfDisposalYearValid.toInt)
  }
}
