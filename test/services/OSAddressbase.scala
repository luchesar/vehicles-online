package services

import org.scalatest.{Matchers, WordSpec}
import scala.io.Source
import java.net.URI
import play.api.libs.json._
import play.api.libs.functional.syntax._

class OSAddressbasePostcodeResponseSpec extends WordSpec with Matchers {

  def getResource(name: String) = Source.fromURL(this.getClass.getResource(s"/$name")).mkString("")

  implicit val uriReads: Reads[URI] = new Reads[URI] {
    override def reads(json: JsValue) = JsSuccess(new URI(json.as[String]))
  }

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


  case class OSAddressbaseLPI(
                               UPRN: String,
                               ADDRESS: String,
                               USRN: String,
                               LPI_KEY : String,
                               LEVEL : Option[String],
                               ORGANISATION : String,
                               SAO_START_NUMBER : Option[String],
                               SAO_START_SUFFIX : Option[String],
                               SAO_END_NUMBER : Option[String],
                               SAO_END_SUFFIX : Option[String],
                               PAO_START_NUMBER : Option[String],
                               PAO_START_SUFFIX : Option[String],
                               PAO_END_NUMBER : Option[String],
                               PAO_END_SUFFIX : Option[String],
                               STREET_DESCRIPTION : String,
                               LOCALITY_NAME : Option[String],
                               TOWN_NAME : Option[String]
                               )

  object OSAddressbaseLPI {
    implicit val readsOSAddressbaseLPI = Json.reads[OSAddressbaseLPI]
  }


  case class OSAddressbaseResult(
                                  DPA: Option[OSAddressbaseDPA]
                                  )

  case class OSAddressbaseHeader(
                                  uri: URI,
                                  query: String,
                                  offset: Int,
                                  totalresults: Int,
                                  format: String,
                                  dataset: String,
                                  maxresults: Int
                                  )

  case class OSAddressbaseSearchResponse(
                                          header: OSAddressbaseHeader,
                                          results: Option[Seq[OSAddressbaseResult]]
                                          )

  "Response Parser loading json for ec1a 4jq" should {
    implicit val readsOSAddressbaseResult = Json.reads[OSAddressbaseResult]
    implicit val readsOSAddressbaseHeader = Json.reads[OSAddressbaseHeader]
    implicit val readsOSAddressbaseSearchResponse = Json.reads[OSAddressbaseSearchResponse]

    "populate the header given json with header but 0 results" in {
      val resp = getResource("osaddressbase_lookup_emptyResult.json")

      val poso = Json.parse(resp).as[OSAddressbaseSearchResponse]

      poso.header.uri should equal(new URI("https://addressapi.ordnancesurvey.co.uk/postcode?&postcode=EC1A+4JQ&dataset=dpa&_=1392379157908"))
      poso.header.totalresults should equal(0)
    }

    "populate the the results given json with 0 result" in {
      val resp = getResource("osaddressbase_lookup_emptyResult.json")

      val poso = Json.parse(resp).as[OSAddressbaseSearchResponse]

      poso.results match {
        case None =>
        case _ => fail("expected results")
      }
    }

    "populate the header given json with header and 1 result" in {
      val resp = getResource("osaddressbase_lookup_oneResult.json")

      val poso = Json.parse(resp).as[OSAddressbaseSearchResponse]

      poso.header.uri should equal(new URI("https://addressapi.ordnancesurvey.co.uk/postcode?&postcode=EC1A+4JQ&dataset=dpa&_=1392379157908"))
      poso.header.totalresults should equal(1)
    }

    "populate the the results given json with 1 result" in {
      val resp = getResource("osaddressbase_lookup_oneResult.json")

      val poso = Json.parse(resp).as[OSAddressbaseSearchResponse]

      poso.results match {
        case Some(results) => results.length should equal(1)
        case _ => fail("expected results")
      }
    }

    "populate the header given json with header and multiple results" in {
      val resp = getResource("OSAddressbase_Lookup_EC1A_4JQ.json")

      val poso = Json.parse(resp).as[OSAddressbaseSearchResponse]

      poso.header.uri should equal(new URI("https://addressapi.ordnancesurvey.co.uk/postcode?&postcode=EC1A+4JQ&dataset=dpa&_=1392379157908"))
      poso.header.totalresults should equal(13)
    }

    "populate the the results given json with multiple results" in {
      val resp = getResource("OSAddressbase_Lookup_EC1A_4JQ.json")

      val poso = Json.parse(resp).as[OSAddressbaseSearchResponse]

      poso.results match {
        case Some(results) => results.length should equal(13)
        case _ => fail("expected results")
      }
    }

  }
}

