package services

import org.scalatest.{Matchers, WordSpec}
import scala.io.Source
import java.net.URI
import play.api.libs.json._

class OSAddressbasePostcodeResponseSpec extends WordSpec with Matchers {

  def getResource(name: String) = Source.fromURL(this.getClass.getResource(s"/$name")).mkString("")

  implicit val uriReads: Reads[URI] = new Reads[URI] {
    override def reads(json: JsValue) = JsSuccess(new URI(json.as[String]))
  }

  case class OSAddressbaseDpa(
                               UPRN: String,
                               ADDRESS: String,
                               ORGANISATION_NAME: Option[String],
                               BUILDING_NUMBER: Option[String],
                               THOROUGHFARE_NAME: String,
                               POST_TOWN: String,
                               POSTCODE: String,
                               RPC: String,
                               X_COORDINATE: Float,
                               Y_COORDINATE: Float,
                               STATUS: String,
                               MATCH: Float,
                               MATCH_DESCRIPTION: String
                               )

  case class OSAddressbaseResult(
                                  DPA: OSAddressbaseDpa
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
    implicit val readsOSAddressbaseDpa = Json.reads[OSAddressbaseDpa]
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

