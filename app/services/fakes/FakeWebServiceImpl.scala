package services.fakes

import play.api.libs.ws.Response
import services.address_lookup.ordnance_survey.domain.{OSAddressbaseHeader, OSAddressbaseSearchResponse, OSAddressbaseResult, OSAddressbaseDPA}
import play.api.libs.json.Json
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import services.address_lookup.gds.domain.{Location, Details, Presentation, Address}
import services.address_lookup.AddressLookupWebService

class FakeWebServiceImpl(responseOfPostcodeWebService: Future[Response],
                         responseOfUprnWebService: Future[Response]) extends AddressLookupWebService {
  override def callPostcodeWebService(postcode: String): Future[Response] =
    if (postcode == FakeAddressLookupService.postcodeInvalid) Future {
      FakeResponse(status = 200, fakeJson = None)
    }
    else responseOfPostcodeWebService

  override def callUprnWebService(postcode: String): Future[Response] = responseOfUprnWebService
}

object FakeWebServiceImpl {
  val uprnValid = 12345L

  def osAddressbaseDPA(houseName: String = "presentationProperty stub", houseNumber: String = "123") = OSAddressbaseDPA(
    UPRN = uprnValid.toString,
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

  def responseValidForOrdnanceSurvey: Future[Response] = {
    val oSAddressbaseResultsValidDPA = {
      val result = OSAddressbaseResult(DPA = Some(osAddressbaseDPA()), LPI = None)
      Seq(result, result, result)
    }
    val inputAsJson = Json.toJson(oSAddressbaseResultsValidDPA)

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
        uprn = uprnValid.toString),
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