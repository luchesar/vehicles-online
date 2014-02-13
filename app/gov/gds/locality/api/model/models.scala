package uk.gov.gds.locality.api.models


import org.joda.time.DateTime



case class Street(usrn: String, streetDescription: String, townName: String, administrativeArea: String)

case class Location(x: Double, y: Double)

case class Details(
                    usrn: String,
                    isResidential: Boolean,
                    isCommercial: Boolean,
                    isPostalAddress: Boolean,
                    classification: String,
                    state: String,
                    organisation: Option[String]
                    // blpuUpdatedAt: DateTime,
                    // blpuCreatedAt: DateTime
                    )

case class Presentation(
                         property: Option[String],
                         street: Option[String],
                         town: Option[String],
                         area: Option[String],
                         postcode: String,
                         uprn: String)

case class Address(
                    gssCode: String,
                    countryCode: String,
                    postcode: String,
                    houseName: Option[String],
                    houseNumber: Option[String],
                    // createdAt: DateTime,
                    presentation: Presentation,
                    details: Details,
                    location: Location)


case class PlacesAddress(
                          lineOne: String,
                          lineTwo: String,
                          lineThree: String,
                          lineFour: String,
                          lineFive: String,
                          county: String,
                          city: String,
                          postcode: String,
                          uprn: String
                          )

object PlacesAddress {
  def fromAddress(address: Address) = PlacesAddress(
    address.presentation.property.getOrElse(address.presentation.street.getOrElse("")),
    if(address.presentation.property.isDefined) address.presentation.street.getOrElse("") else "",
    "",
    "",
    "",
    address.presentation.area.getOrElse(""),
    address.presentation.town.getOrElse(""),
    address.presentation.postcode,
    address.presentation.uprn
  )
}

case class Ero(telephoneNumber: String = "")
case class LocalAuthority(gssId: String, opcsId: String, name: String, ero: Ero = Ero())

object JsonFormats {

  import play.api.libs.json._

  val pattern = "yyyy-MM-dd'T'HH:mm:ssz"
  implicit val dateFormat =
    Format[DateTime](Reads.jodaDateReads(pattern), Writes.jodaDateWrites(pattern))

  // Generates Writes and Reads for Feed and User thanks to Json Macros
  implicit val streetFormat = Json.format[Street]
  implicit val locationFormat = Json.format[Location]
  implicit val presentationFormat = Json.format[Presentation]
  implicit val detailsFormat = Json.format[Details]
  implicit val addressFormat = Json.format[Address]
  implicit val placesAddressFormat = Json.format[PlacesAddress]
  implicit val eroFormat = Json.format[Ero]
  implicit val authorityFormat = Json.format[LocalAuthority]

  implicit val streetWrite = Json.writes[Street]
  implicit val locationWrite = Json.writes[Location]
  implicit val presentationWrite = Json.writes[Presentation]
  implicit val detailsWrite = Json.writes[Details]
  implicit val addressWrite = Json.writes[Address]
  implicit val placesAddressWrite = Json.writes[PlacesAddress]
  implicit val eroWrite = Json.writes[Ero]
  implicit val authorityWrite = Json.writes[LocalAuthority]
}
