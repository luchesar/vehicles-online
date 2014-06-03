package models.domain.disposal_of_vehicle

import play.api.libs.json.Json
import org.joda.time.DateTime

final case class DisposeRequest(referenceNumber: String,
                                registrationNumber: String,
                                traderName: String,
                                traderAddress: DisposalAddressDto,
                                dateOfDisposal: String,
                                transactionTimestamp: String,
                                prConsent: Boolean,
                                keeperConsent: Boolean,
                                trackingId: String,
                                mileage: Option[Int] = None)

object DisposeRequest {
  implicit val JsonFormat = Json.writes[DisposeRequest]
}