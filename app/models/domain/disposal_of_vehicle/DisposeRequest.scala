package models.domain.disposal_of_vehicle

case class DisposeRequest(referenceNumber: String,
                          registrationNumber: String,
                          traderName: String,
                          disposalAddress: DisposalAddressDto,
                          dateOfDisposal: String,
                          transactionTimestamp: String,
                          mileage: Option[Int] = None,
                          ipAddress: Option[String] = None)

object DisposeRequest {
  import play.api.libs.json.Json
  implicit val disposeRequestFormat = Json.format[DisposeRequest]
}