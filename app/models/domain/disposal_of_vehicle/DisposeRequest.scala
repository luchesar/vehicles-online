package models.domain.disposal_of_vehicle

import play.api.libs.json.Json

final case class DisposeRequest(referenceNumber: String,
                          registrationNumber: String,
                          traderName: String,
                          traderAddress: DisposalAddressDto,
                          dateOfDisposal: String,
                          transactionTimestamp: String,
                          mileage: Option[Int] = None,
                          ipAddress: Option[String] = None)

object DisposeRequest {
  implicit val JsonFormat = Json.writes[DisposeRequest]
}