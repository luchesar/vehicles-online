package models.domain.disposal_of_vehicle


case class DisposeResponse (transactionId: String, registrationNumber: String, auditId: String, responseCode: Option[String] = None)

object DisposeResponse{
  import play.api.libs.json.Json
  implicit val disposeResponseFormat = Json.format[DisposeResponse]
}