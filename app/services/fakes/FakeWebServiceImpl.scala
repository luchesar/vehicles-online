package services.fakes

import play.api.libs.ws.Response
import services.WebService
import services.address_lookup.ordnance_survey.domain.{OSAddressbaseResult, OSAddressbaseDPA}
import play.api.libs.json.Json
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import services.address_lookup.gds.domain.{Location, Details, Presentation, Address}

class FakeWebServiceImpl(responseOfPostcodeWebService: Future[Response],
                         responseOfUprnWebService: Future[Response]) extends WebService {
  override def callPostcodeWebService(postcode: String): Future[Response] = responseOfPostcodeWebService

  override def callUprnWebService(postcode: String): Future[Response] = responseOfUprnWebService
}

object FakeWebServiceImpl {
  val uprnValid = "1"

  def responseValidForOrdnanceSurvey: Future[Response] = {
    def oSAddressbaseDPA(houseName: String = "houseName stub", houseNumber: String = "123") = OSAddressbaseDPA(
      UPRN = uprnValid,
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

    val oSAddressbaseResultsValidDPA = {
      val result = OSAddressbaseResult(DPA = Some(oSAddressbaseDPA()), LPI = None)
      Seq(result, result, result)
    }
    val inputAsJson = Json.toJson(oSAddressbaseResultsValidDPA)

    Future {
      FakeResponse(status = 200, fakeJson = Some(inputAsJson))
    }
  }

  def responseValidForGdsAddressLookup: Future[Response] = {
    def gdsAddressValid(houseName: String = "houseName stub", houseNumber: String = "123"): Address =
      Address(
        gssCode = "gssCode stub",
        countryCode = "countryCode stub",
        postcode = "postcode stub",
        houseName = Some(houseName),
        houseNumber = Some(houseNumber),
        presentation = Presentation(property = Some("property stub"),
          street = Some("street stub"),
          town = Some("town stub"),
          area = Some("area stub"),
          postcode = "postcode stub",
          uprn = uprnValid),
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

    import services.address_lookup.gds.domain.JsonFormats._
    val inputAsJson = Json.toJson(Seq(gdsAddressValid()))

    Future {
      FakeResponse(status = 200, fakeJson = Some(inputAsJson))
    }
  }
}