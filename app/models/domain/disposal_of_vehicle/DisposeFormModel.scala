package models.domain.disposal_of_vehicle

import models.DayMonthYear

case class DisposeFormModel(mileage: Option[Int], dateOfDisposal: DayMonthYear, consent: String, lossOfRegistrationConsent: String)