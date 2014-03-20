package models.domain.disposal_of_vehicle

import models.DayMonthYear

case class DisposeFormModel(mileage:Option[Int] = None, dateOfDisposal: DayMonthYear, emailAddress: Option[String] = None)