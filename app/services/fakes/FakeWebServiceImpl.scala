package services.fakes

import play.api.libs.ws.Response
import services.address_lookup.ordnance_survey.domain.{OSAddressbaseHeader, OSAddressbaseSearchResponse, OSAddressbaseResult, OSAddressbaseDPA}
import play.api.libs.json.Json
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import services.address_lookup.gds.domain.{Location, Details, Presentation, Address}
import services.address_lookup.AddressLookupWebService
import java.net.URI

class FakeWebServiceImpl(responseOfPostcodeWebService: Future[Response],
                         responseOfUprnWebService: Future[Response]) extends AddressLookupWebService {
  override def callPostcodeWebService(postcode: String): Future[Response] =
    if (postcode == FakeAddressLookupService.postcodeInvalid) Future {
      FakeResponse(status = 200, fakeJson = None)
    }
    else responseOfPostcodeWebService

  override def callUprnWebService(uprn: String): Future[Response] = responseOfUprnWebService
}

object FakeWebServiceImpl {
  val traderUprnValid = 12345L
  val traderUprnValid2 = 4567L

  def osAddressbaseDPA(uprn: String = traderUprnValid.toString, houseName: String = "presentationProperty stub", houseNumber: String = "123") = OSAddressbaseDPA(
    UPRN = uprn,
    address = s"$houseName, $houseNumber, property stub, street stub, town stub, area stub, postcode stub",
    buildingName = Some(houseName),
    buildingNumber = Some(houseNumber),
    postTown = "b",
    postCode = "c",
    RPC = "d",
    xCoordinate = 1f,
    yCoordinate = 2f,
    status = "e",
    matchScore = 3f,
    matchDescription = "f"
  )

  private val header = OSAddressbaseHeader(uri = new URI(""),
    query="",
    offset=0,
    totalresults=2,
    format="",
    dataset="",
    maxresults=2)

  def responseValidForOrdnanceSurvey: Future[Response] = {
    val results = {
      val result1 = OSAddressbaseResult(DPA = Some(osAddressbaseDPA()), LPI = None)
      val result2 = OSAddressbaseResult(DPA = Some(osAddressbaseDPA(uprn = "67890", houseNumber = "456")), LPI = None)
      val result3 = OSAddressbaseResult(DPA = Some(osAddressbaseDPA(uprn = "111213", houseNumber = "789")), LPI = None)

      Seq(result1, result2, result3)
    }
    val response = OSAddressbaseSearchResponse(header = header,
      results = Some(results))
    val inputAsJson = Json.toJson(response)

    Future {
      FakeResponse(status = 200, fakeJson = Some(inputAsJson))
    }
  }

  def responseValidForOrdnanceSurveyNotFound: Future[Response] = {
    val response = OSAddressbaseSearchResponse(header = header,
      results = None)
    val inputAsJson = Json.toJson(response)

    Future {
      FakeResponse(status = 200, fakeJson = Some(inputAsJson))
    }
  }


  def gdsAddress(presentationProperty: String = "property stub", presentationStreet: String = "123"): Address =
    Address(
      gssCode = "gssCode stub",
      countryCode = "countryCode stub",
      postcode = "postcode stub",
      houseName = Some("presentationProperty stub"),
      houseNumber = Some("123"),
      presentation = Presentation(property = Some(presentationProperty),
        street = Some(presentationStreet),
        town = Some("town stub"),
        area = Some("area stub"),
        postcode = "postcode stub",
        uprn = traderUprnValid.toString),
      details = Details(
        usrn = "usrn stub",
        isResidential = true,
        isCommercial = true,
        isPostalAddress = true,
        classification = "classification stub",
        state = "state stub",
        organisation = Some("organisation stub")
      ),
      location = Location(
        x = 1.0d,
        y = 2.0d)
    )

  def responseValidForGdsAddressLookup: Future[Response] = {
    import services.address_lookup.gds.domain.JsonFormats._
    val inputAsJson = Json.toJson(Seq(gdsAddress(), gdsAddress(presentationStreet = "456")))

    Future {
      FakeResponse(status = 200, fakeJson = Some(inputAsJson))
    }
  }
}