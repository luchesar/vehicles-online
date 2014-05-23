package models.domain.disposal_of_vehicle

import play.api.libs.json.Json

final case class DisposeRequest(referenceNumber: String,
                          registrationNumber: String,
                          traderName: String,
                          disposalAddress: DisposalAddressDto,
                          dateOfDisposal: String,
                          transactionTimestamp: String,
                          mileage: Option[Int] = None,
                          ipAddress: Option[String] = None)

object DisposeRequest {
  implicit val JsonFormat = Json.format[DisposeRequest] // TODO US66 if we only ever write and never read then we can change the 'Json.format' to 'Json.writes' to reduce the compilation.
}