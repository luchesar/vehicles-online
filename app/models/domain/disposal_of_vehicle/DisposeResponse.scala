package models.domain.disposal_of_vehicle

import play.api.libs.json.Json

final case class DisposeResponse (transactionId: String, registrationNumber: String, auditId: String, responseCode: Option[String] = None)

object DisposeResponse{
  implicit val JsonFormat = Json.format[DisposeResponse]
}