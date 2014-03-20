package services.fakes

import play.api.libs.ws.{WS, Response}
import services.WebService
import scala.concurrent.Future
import services.address_lookup.ordnance_survey.domain.{OSAddressbaseResult, OSAddressbaseDPA}
import play.api.libs.json.Json

class FakeWebServiceImpl (responseOfPostcodeWebService: Future[Response] = FakeWebServiceImpl.responseValid,
                         responseOfUprnWebService: Future[Response] = FakeWebServiceImpl.responseValid) extends WebService {
  override def callPostcodeWebService(postcode: String): Future[Response] = responseOfPostcodeWebService

  override def callUprnWebService(postcode: String): Future[Response] = responseOfUprnWebService
}

object FakeWebServiceImpl {
  val responseValid = {
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

    WS.url("http://localhost:9000/"). // Needs a valid format of URL, but we will never call it
      post(inputAsJson)
  }
}
