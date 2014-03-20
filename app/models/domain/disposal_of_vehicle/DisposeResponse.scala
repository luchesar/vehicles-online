package models.domain.disposal_of_vehicle

import play.api.libs.json.Json

case class DisposeResponse (success: Boolean, message: String, transactionId: String, registrationNumber: String, auditId: String)

object DisposeResponse{
  implicit val disposeResponseFormat = Json.format[DisposeResponse]
}