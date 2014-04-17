package services.fakes

import services.address_lookup.ordnance_survey.domain.{OSAddressbaseHeader, OSAddressbaseSearchResponse, OSAddressbaseResult}
import play.api.libs.json.Json
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import services.address_lookup.gds.domain.Address
import services.address_lookup.AddressLookupWebService
import java.net.URI
import play.api.http.Status._
import play.api.libs.ws.Response
import services.address_lookup.gds.domain.Location
import scala.Some
import services.address_lookup.gds.domain.Details
import services.address_lookup.gds.domain.Presentation
import services.address_lookup.gds.domain.Address
import services.fakes.FakeAddressLookupService.{postcodeValid, postcodeInvalid}
import models.domain.disposal_of_vehicle.{UprnAddressPair, PostcodeToAddressResponse}

class FakeWebServiceImpl(responseOfPostcodeWebService: Future[Response],
                         responseOfUprnWebService: Future[Response]) extends AddressLookupWebService {
  override def callPostcodeWebService(postcode: String): Future[Response] =
    if (postcode == postcodeInvalid) Future {
      FakeResponse(status = OK, fakeJson = None)
    }
    else responseOfPostcodeWebService

  override def callUprnWebService(uprn: String): Future[Response] = responseOfUprnWebService
}

object FakeWebServiceImpl {
  val traderUprnValid = 12345L
  val traderUprnValid2 = 4567L

  def uprnAddressPairWithDefaults(uprn: String = traderUprnValid.toString, houseName: String = "presentationProperty stub", houseNumber: String = "123") =
    UprnAddressPair(uprn, address = s"$houseName, $houseNumber, property stub, street stub, town stub, area stub, $postcodeValid")

  private val header = OSAddressbaseHeader(uri = new URI(""),
    query = "",
    offset = 0,
    totalresults = 2,
    format = "",
    dataset = "",
    maxresults = 2)

  def postcodeToAddressResponseValid: PostcodeToAddressResponse = {
    val results = Seq(
      uprnAddressPairWithDefaults(),
      uprnAddressPairWithDefaults(uprn = "67890", houseNumber = "456"),
      uprnAddressPairWithDefaults(uprn = "111213", houseNumber = "789")
    )

    PostcodeToAddressResponse(addresses = results)
  }

  def responseValidForOrdnanceSurvey: Future[Response] = {
    val inputAsJson = Json.toJson(postcodeToAddressResponseValid)

    Future {
      FakeResponse(status = OK, fakeJson = Some(inputAsJson))
    }
  }

  def responseValidForOrdnanceSurveyNotFound: Future[Response] = {
    val response = OSAddressbaseSearchResponse(header = header,
      results = None)
    val inputAsJson = Json.toJson(response)

    Future {
      FakeResponse(status = OK, fakeJson = Some(inputAsJson))
    }
  }

  def gdsAddress(presentationProperty: String = "property stub", presentationStreet: String = "123"): Address =
    Address(
      gssCode = "gssCode stub",
      countryCode = "countryCode stub",
      postcode = postcodeValid,
      houseName = Some("presentationProperty stub"),
      houseNumber = Some("123"),
      presentation = Presentation(property = Some(presentationProperty),
        street = Some(presentationStreet),
        town = Some("town stub"),
        area = Some("area stub"),
        postcode = postcodeValid,
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
      FakeResponse(status = OK, fakeJson = Some(inputAsJson))
    }
  }
}