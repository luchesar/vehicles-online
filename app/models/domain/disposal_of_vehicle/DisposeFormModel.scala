package models.domain.disposal_of_vehicle

import models.DayMonthYear

case class DisposeFormModel(consent: Boolean, mileage:Option[Int], dateOfDisposal: DayMonthYear = DayMonthYear(None, None, None))