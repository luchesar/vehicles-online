package models.domain.disposal_of_vehicle

import models.DayMonthYear
import play.api.libs.json.Json

case class DisposeModel(referenceNumber: String,
                        registrationNumber: String,
                        dateOfDisposal: DayMonthYear,
                        mileage: Option[Int])
