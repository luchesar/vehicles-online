package models.domain.disposal_of_vehicle

import models.DayMonthYear

case class DisposeModel(referenceNumber: String,
                        registrationNumber: String,
                         dateOfDisposal: DayMonthYear)
