package models.domain.disposal_of_vehicle


case class DisposeResponse (success: Boolean, message: String, transactionId: String, registrationNumber: String, auditId: String)

object DisposeResponse{
  import play.api.libs.json.Json
  implicit val disposeResponseFormat = Json.format[DisposeResponse]
}