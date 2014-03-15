package services.address_lookup.ordnance_survey

import services.fakes.FakeWebServiceImpl
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import services.address_lookup._
import helpers.disposal_of_vehicle.PostcodePage.postcodeValid
import org.mockito.Mockito._
import services.address_lookup.ordnance_survey.domain._
import play.api.libs.json.Json
import java.net.URI
import play.api.libs.ws.Response
import services.AddressLookupService
import play.api.libs.json.JsValue
import helpers.UnitSpec

class OSAddressLookupServiceSpec extends UnitSpec {
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

  val oSAddressbaseResultsEmptyDPAAndLPI = {
    val result = OSAddressbaseResult(DPA = None, LPI = None)

    Seq(result, result, result)
  }

  def addressServiceMock(response: Future[Response], results: Option[Seq[OSAddressbaseResult]]): AddressLookupService = {
    // This class allows overriding of the base classes methods which call the real web service.
    class PartialAddressService(responseOfPostcodeWebService: Future[Response],
                                responseOfUprnWebService: Future[Response],
                                results: Option[Seq[OSAddressbaseResult]]) extends ordnance_survey.AddressLookupServiceImpl(new FakeWebServiceImpl) {

      override protected def callPostcodeWebService(postcode: String): Future[Response] = responseOfPostcodeWebService

      override protected def callUprnWebService(uprn: String): Future[Response] = responseOfUprnWebService

      //override def extractFromJson(resp: Response): Option[Seq[OSAddressbaseResult]] = results
    }

    new PartialAddressService(
      responseOfPostcodeWebService = response,
      responseOfUprnWebService = response,
      results = results)
  }

  def response(statusCode: Int, inputAsJson: JsValue): Future[Response] = Future {
    val response = mock[Response] // It's very hard to create a Response so use a mock.
    when(response.status).thenReturn(statusCode)
    when(response.json).thenReturn(inputAsJson)
    response
  }

  def response(statusCode: Int,
               input: OSAddressbaseSearchResponse = OSAddressbaseSearchResponse(header = header, results = Some(oSAddressbaseResultsValidDPA))): Future[Response] = {
    val inputAsJson = Json.toJson(input)
    response(statusCode, inputAsJson)
  }

  def responseThrows = Future {
    val response = mock[Response]
    when(response.status).thenThrow(new RuntimeException("This error is generated deliberately by a test"))
    response
  }

  def header = {
    val uri = new URI("http://example.org/")
    val header = mock[OSAddressbaseHeader]
    when(header.uri).thenReturn(uri)
    header
  }

  "fetchAddressesForPostcode" should {
    "return seq when response status is 200 and returns results" in {
      val service = addressServiceMock(response(200), Some(oSAddressbaseResultsValidDPA))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        r =>
          r.length should equal(oSAddressbaseResultsValidDPA.length)
          r should equal(oSAddressbaseResultsValidDPA.map(i => (i.DPA.get.UPRN, i.DPA.get.address)))
      }
    }

    "return empty seq when response status is Ok but results is empty" in {
      val input = OSAddressbaseSearchResponse(header = header, results = None)
      val service = addressServiceMock(response(200, input), None)

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return empty seq when response status is not Ok (200)" in {
      val service = addressServiceMock(response(404), Some(oSAddressbaseResultsValidDPA))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return empty seq when the result has no DPA and no LPI" in {
      val input = OSAddressbaseSearchResponse(header = header, results = None)
      val service = addressServiceMock(response(200, input), Some(oSAddressbaseResultsEmptyDPAAndLPI))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return empty seq when response throws" in {
      val addressLookupService = addressServiceMock(responseThrows, None)

      val result = addressLookupService.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return empty seq given invalid json" in {
      val inputAsJson = Json.toJson("INVALID")
      val service = addressServiceMock(response(200, inputAsJson), Some(oSAddressbaseResultsValidDPA))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "not throw when an address contains a building number that contains letters" in {
      val expected = Seq(
        (uprnValid, "houseName AAA, 123A, property stub, street stub, town stub, area stub, postcode stub"),
        (uprnValid, "houseName BBB, 123B, property stub, street stub, town stub, area stub, postcode stub"),
        (uprnValid, "houseName stub, 789C, property stub, street stub, town stub, area stub, postcode stub")
      )
      val dpa1 = OSAddressbaseResult(DPA = Some(oSAddressbaseDPA(houseNumber = "789C")), LPI = None)
      val dpa2 = OSAddressbaseResult(DPA = Some(oSAddressbaseDPA(houseName = "houseName BBB", houseNumber = "123B")), LPI = None)
      val dpa3 = OSAddressbaseResult(DPA = Some(oSAddressbaseDPA(houseName = "houseName AAA", houseNumber = "123A")), LPI = None)
      val oSAddressbaseResultsValidDPA = Seq(dpa1, dpa2, dpa3)

      val input = OSAddressbaseSearchResponse(header = header, results = Some(oSAddressbaseResultsValidDPA))
      val service = addressServiceMock(response(200, input), Some(oSAddressbaseResultsValidDPA))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        r =>
          r.length should equal(oSAddressbaseResultsValidDPA.length)
          r shouldBe expected
      }
    }

    "return seq of (uprn, address) sorted by building number then building name" in {
      val expected = Seq(
        (uprnValid, "houseName AAA, 123, property stub, street stub, town stub, area stub, postcode stub"),
        (uprnValid, "houseName BBB, 123, property stub, street stub, town stub, area stub, postcode stub"),
        (uprnValid, "houseName stub, 789, property stub, street stub, town stub, area stub, postcode stub")
      )
      val dpa1 = OSAddressbaseResult(DPA = Some(oSAddressbaseDPA(houseNumber = "789")), LPI = None)
      val dpa2 = OSAddressbaseResult(DPA = Some(oSAddressbaseDPA(houseName = "houseName BBB", houseNumber = "123")), LPI = None)
      val dpa3 = OSAddressbaseResult(DPA = Some(oSAddressbaseDPA(houseName = "houseName AAA", houseNumber = "123")), LPI = None)
      val oSAddressbaseResultsValidDPA = Seq(dpa1, dpa2, dpa3)

      val input = OSAddressbaseSearchResponse(header = header, results = Some(oSAddressbaseResultsValidDPA))
      val service = addressServiceMock(response(200, input), Some(oSAddressbaseResultsValidDPA))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        r =>
          r.length should equal(oSAddressbaseResultsValidDPA.length)
          r shouldBe expected
      }
    }
  }

  "postcodeWithNoSpaces" should {
    import helpers.disposal_of_vehicle.PostcodePage.{postcodeValid, postcodeValidWithSpace}
    val addressLookupService = new AddressLookupServiceImpl(ws = new FakeWebServiceImpl)

    "return the same string if no spaces present" in {
      val result = addressLookupService.postcodeWithNoSpaces(postcodeValid)

      result should equal(postcodeValid)
    }

    "remove spaces when present" in {
      val result = addressLookupService.postcodeWithNoSpaces(postcodeValidWithSpace)

      result should equal(postcodeValid)
    }
  }

  "fetchAddressForUprn" should {
    "return AddressViewModel when response status is 200" in {
      val service = addressServiceMock(response(200), Some(oSAddressbaseResultsValidDPA))

      val result = service.fetchAddressForUprn(oSAddressbaseDPA().UPRN)

      whenReady(result) {
        case Some(addressViewModel) =>
          addressViewModel.uprn.map(_.toString) should equal(Some(oSAddressbaseDPA().UPRN))
          addressViewModel.address === oSAddressbaseDPA().address
        case _ => fail("Should have returned Some(AddressViewModel)")
      }
    }

    "return None when response status is not 200" in {
      val service = addressServiceMock(response(404), Some(oSAddressbaseResultsValidDPA))

      val result = service.fetchAddressForUprn(oSAddressbaseDPA().UPRN)

      whenReady(result) {
        _ should equal(None)
      }
    }

    "return none when response status is Ok but results is empty" in {
      val input = OSAddressbaseSearchResponse(header = header, results = None)
      val service = addressServiceMock(response(200, input), None)

      val result = service.fetchAddressForUprn(oSAddressbaseDPA().UPRN)

      whenReady(result) {
        _ should equal(None)
      }
    }

    "return none when the result has no DPA and no LPI" in {
      val input = OSAddressbaseSearchResponse(header = header, results = Some(oSAddressbaseResultsEmptyDPAAndLPI))
      val service = addressServiceMock(response(200, input), Some(oSAddressbaseResultsEmptyDPAAndLPI))

      val result = service.fetchAddressForUprn(oSAddressbaseDPA().UPRN)

      whenReady(result) {
        _ should equal(None)
      }
    }

    "return none when web service throws an exception" in {
      val addressLookupService = addressServiceMock(responseThrows, None)

      val result = addressLookupService.fetchAddressForUprn(oSAddressbaseDPA().UPRN)

      whenReady(result) {
        _ should equal(None)
      }
    }
  }
}
