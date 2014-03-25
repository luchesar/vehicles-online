package services.fakes

import play.api.libs.ws.Response
import services.WebService
import services.address_lookup.ordnance_survey.domain.{OSAddressbaseResult, OSAddressbaseDPA}
import play.api.libs.json.Json
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

class FakeWebServiceImpl(responseOfPostcodeWebService: Future[Response],
                         responseOfUprnWebService: Future[Response]) extends WebService {
  override def callPostcodeWebService(postcode: String): Future[Response] = responseOfPostcodeWebService

  override def callUprnWebService(postcode: String): Future[Response] = responseOfUprnWebService
}

object FakeWebServiceImpl {
  val responseValidForOrdnanceSurvey = {
    val uprnValid = "1"

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
}