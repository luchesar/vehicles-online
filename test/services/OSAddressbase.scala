package services

import org.scalatest.{Matchers, WordSpec}
import scala.io.Source
import java.net.URI
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scalaz.std.list
import scalaz.std

class OSAddressbasePostcodeResponseSpec extends WordSpec with Matchers {

  def getResource(name: String) = Source.fromURL(this.getClass.getResource(s"/$name")).mkString("")

  implicit val uriReads: Reads[URI] = new Reads[URI] {
    override def reads(json: JsValue) = JsSuccess(new URI(json.as[String]))
  }

  case class OSAddressbaseDPASearchResult(
    UPRN: String,
    address: String,
    organisationName: String,
    buildingNumber: String,
    thoroughfareName: String,
    postTown: String,
    postCode: String,
    RPC: String,
    xCordinate: Float,
    yCordinate: Float,
    status: String,
    matchScore: Float,
    matchDescription: String
  )

  object OSAddressbaseDPASearchResult {
    implicit val reads: Reads[OSAddressbaseDPASearchResult] = (
      (__ \ "UPRN").read[String] and
        (__ \ "address").read[String] and
        (__ \ "organisationName").read[String] and
        (__ \ "buildingNumber").read[String] and
        (__ \ "thoroughfareName").read[String] and
        (__ \ "postTown").read[String] and
        (__ \ "postCode").read[String] and
        (__ \ "RPC").read[String] and
        (__ \ "xCordinate").read[Float] and
        (__ \ "yCordinate").read[Float] and
        (__ \ "status").read[String] and
        (__ \ "matchScore").read[Float] and
        (__ \ "matchDescription").read[String]
      )(OSAddressbaseDPASearchResult.apply _)
  }

  case class OSAddressbaseHeader(
                                  uri: URI,
                                  query: String,
                                  offset: Int,
                                  totalResults: Int,
                                  format: String,
                                  dataset: String,
                                  maxResults: Int
                                  )

  object OSAddressbaseHeader {
    implicit val reads: Reads[OSAddressbaseHeader] = (
      (__ \ "uri").read[URI] and
        (__ \ "query").read[String] and
        (__ \ "offset").read[Int] and
        (__ \ "totalresults").read[Int] and
        (__ \ "format").read[String] and
        (__ \ "dataset").read[String] and
        (__ \ "maxresults").read[Int]
      )(OSAddressbaseHeader.apply _)
  }

  case class OSAddressbaseSearchResponse(header: OSAddressbaseHeader, dpaResults: List[OSAddressbaseDPASearchResult])

  object OSAddressbaseSearchResponse {
    implicit val reads: Reads[OSAddressbaseSearchResponse] = (
      (JsPath \ "header").read[OSAddressbaseHeader] and
      (JsPath \ 'results \\ 'DPA).read[List[OSAddressbaseDPASearchResult]]
      )(OSAddressbaseSearchResponse.apply _)
  }

  "Response Parser loading json for ec1a 4jq" should {
    "populate the header" in {
      val json = getResource("osaddressbase_lookup_ec1a_4jq.json")
      val poso = Json.fromJson[OSAddressbaseSearchResponse](Json.parse(json)).get
      poso.header.uri should equal(new URI("https://addressapi.ordnancesurvey.co.uk/postcode?&postcode=EC1A+4JQ&dataset=dpa&_=1392379157908"))
      poso.header.totalResults should equal (13)
    }

    "populate the DPA results" in {
      val json = getResource("osaddressbase_lookup_ec1a_4jq.json")
      val jsresult = Json.fromJson[OSAddressbaseSearchResponse](Json.parse(json))
      val poso = jsresult.get
      poso.dpaResults.length should equal(13)
    }
  }
}

