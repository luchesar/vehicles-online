package models.domain.disposal_of_vehicle


case class DisposeRequest(referenceNumber: String,
                        registrationNumber: String,
                        dateOfDisposal: String,
                        mileage: Option[Int])

object DisposeRequest {
  import play.api.libs.json.Json
  implicit val disposeRequestFormat = Json.format[DisposeRequest]
}