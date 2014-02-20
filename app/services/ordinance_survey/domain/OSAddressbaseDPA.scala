package services.ordinance_survey.domain

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class OSAddressbaseDPA(
                             UPRN: String,
                             address: String,
                             poBoxNumber: Option[String],
                             organisationName: Option[String],
                             departmentName: Option[String],
                             subBuildingName: Option[String],
                             buildingName: Option[String],
                             buildingNumber: Option[String],
                             dependentThoroughfareName: Option[String],
                             thoroughfareName: Option[String],
                             doubleDependentLocality: Option[String],
                             dependentLocality: Option[String],
                             postTown: String,
                             postCode: String,
                             RPC: String,
                             xCordinate: Float,
                             yCordinate: Float,
                             status: String,
                             matchScore: Float,
                             matchDescription: String
                             )

object OSAddressbaseDPA {
  implicit val reads: Reads[OSAddressbaseDPA] = (
    (__ \ "UPRN").read[String] and
      (__ \ "ADDRESS").read[String] and
      (__ \ "PO_BOX_NUMBER").readNullable[String] and
      (__ \ "ORGANISATION_NAME").readNullable[String] and
      (__ \ "DEPARTMEMT_NAME").readNullable[String] and
      (__ \ "SUB_BUILDING_NAME").readNullable[String] and
      (__ \ "BUILDING_NAME").readNullable[String] and
      (__ \ "BUILDING_NUMBER").readNullable[String] and
      (__ \ "DEPENDENT_THOROUGHFARE_NAME").readNullable[String] and
      (__ \ "THOROUGHFARE_NAME").readNullable[String] and
      (__ \ "DOUBLE_DEPENDENT_LOCALITY").readNullable[String] and
      (__ \ "DEPENDENT_LOCALITY").readNullable[String] and
      (__ \ "POST_TOWN").read[String] and
      (__ \ "POSTCODE").read[String] and
      (__ \ "RPC").read[String] and
      (__ \ "X_COORDINATE").read[Float] and
      (__ \ "Y_COORDINATE").read[Float] and
      (__ \ "STATUS").read[String] and
      (__ \ "MATCH").read[Float] and
      (__ \ "MATCH_DESCRIPTION").read[String]
    )(OSAddressbaseDPA.apply _)
}
