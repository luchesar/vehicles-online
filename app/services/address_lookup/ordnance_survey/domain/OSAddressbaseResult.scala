package services.address_lookup.ordnance_survey.domain

import play.api.libs.json.Json

case class OSAddressbaseResult(
                                DPA: Option[OSAddressbaseDPA],
                                LPI: Option[OSAddressbaseLPI]
                                )

object OSAddressbaseResult {
  implicit val readsOSAddressbaseResult = Json.format[OSAddressbaseResult]
}