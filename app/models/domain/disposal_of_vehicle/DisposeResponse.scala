package models.domain.disposal_of_vehicle

import play.api.libs.json.Json

case class DisposeResponse (message: String, transactionId: String, registrationNumber: String, auditId: String, responseCode: Option[String] = None)

object DisposeResponse{
  implicit final val JsonFormat = Json.format[DisposeResponse]
}