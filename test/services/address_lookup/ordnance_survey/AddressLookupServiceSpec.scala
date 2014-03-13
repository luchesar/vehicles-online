package services.address_lookup.ordnance_survey

import services.fakes.FakeWebServiceImpl
import org.scalatest.mock.MockitoSugar
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import services.address_lookup._
import helpers.disposal_of_vehicle.PostcodePage.postcodeValid
import org.mockito.Mockito._
import services.address_lookup.ordnance_survey.domain._
import play.api.libs.json.Json
import java.net.URI
import play.api.libs.ws.Response
import org.scalatest._
import org.scalatest.concurrent._
import services.AddressLookupService

class AddressLookupServiceSpec extends WordSpec with ScalaFutures with Matchers with MockitoSugar {
  val oSAddressbaseDPA = OSAddressbaseDPA(
    UPRN = "1",
    address = "a",
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
    val result = OSAddressbaseResult(DPA = Some(oSAddressbaseDPA), LPI = None)
    Seq(result, result, result)
  }

  val oSAddressbaseResultsEmptyDPAAndLPI = {
    val result = OSAddressbaseResult(DPA = None, LPI = None)

    Seq(result, result, result)
  }

  def addressServiceMock(response: Response, results: Option[Seq[OSAddressbaseResult]]): AddressLookupService = {
    // This class allows overriding of the base classes methods which call the real web service.
    class PartialMockAddressLookupService(ws: services.WebService = new FakeWebServiceImpl,
                                          responseOfPostcodeWebService: Future[Response] = Future {
                                            mock[Response]
                                          },
                                          responseOfUprnWebService: Future[Response] = Future {
                                            mock[Response]
                                          },
                                          results: Option[Seq[OSAddressbaseResult]]) extends ordnance_survey.AddressLookupServiceImpl(ws) {

      override protected def callPostcodeWebService(postcode: String): Future[Response] = responseOfPostcodeWebService

      override protected def callUprnWebService(uprn: String): Future[Response] = responseOfUprnWebService

      override def extractFromJson(resp: Response): Option[Seq[OSAddressbaseResult]] = results
    }

    new PartialMockAddressLookupService(
      responseOfPostcodeWebService = Future {
        response
      },
      responseOfUprnWebService = Future {
        response
      },
      results = results)
  }

  // Wrap simple response status code in a Response (mock).
  def addressServiceMock(statusCode: Int, results: Option[Seq[OSAddressbaseResult]]): AddressLookupService = {
    val response = mock[Response]
    when(response.status).thenReturn(statusCode)
    addressServiceMock(response, results)
  }

  "fetchAddressesForPostcode" should {
    "return seq when response status is 200 and returns results" in {
      val service = addressServiceMock(200, Some(oSAddressbaseResultsValidDPA))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        r =>
          r.length should equal(oSAddressbaseResultsValidDPA.length)
          r should equal(oSAddressbaseResultsValidDPA.map(i => (i.DPA.get.UPRN, i.DPA.get.address)))
      }
    }

    "return empty seq when response status is Ok but results is empty" in {
      val service = addressServiceMock(200, None)

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return empty seq when response status is not Ok (200)" in {
      val service = addressServiceMock(404, Some(oSAddressbaseResultsValidDPA))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return empty seq when the result has no DPA and no LPI" in {
      val service = addressServiceMock(200, Some(oSAddressbaseResultsEmptyDPAAndLPI))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return empty seq when web service throws an exception" in {
      val response = mock[Response]
      when(response.status).thenThrow(new RuntimeException("This error is generated deliberately by a test"))
      val addressLookupService = addressServiceMock(response, None)

      val result = addressLookupService.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
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

  "extract from json" should {
    val oSAddressbaseHeader = {
      val uri = new URI("a")
      val query = "b"
      val offset = 2
      val totalresults = 1
      val format = "c"
      val dataset = "d"
      val maxresults = 3

      OSAddressbaseHeader(
        uri = uri,
        query = query,
        offset = offset,
        totalresults = totalresults,
        format = format,
        dataset = dataset,
        maxresults = maxresults
      )
    }

    val addressLookupService = new AddressLookupServiceImpl(ws = new FakeWebServiceImpl)


    "return expected given valid json with no results" in {
      val expectedResults: Option[Seq[OSAddressbaseResult]] = None

      val inputAsJson = {
        val input = OSAddressbaseSearchResponse(
          header = oSAddressbaseHeader,
          results = expectedResults)
        Json.toJson(input)
      }

      val response = mock[Response] // It's very hard to create a Response so use a mock.
      when(response.json).thenReturn(inputAsJson)

      val result = addressLookupService.extractFromJson(response)

      result should equal(None)
    }

    "return expected given valid json with results" in {
      val expectedResults: Option[Seq[OSAddressbaseResult]] = Some(oSAddressbaseResultsValidDPA)

      val inputAsJson = {
        val input = OSAddressbaseSearchResponse(
          header = oSAddressbaseHeader,
          results = expectedResults)
        Json.toJson(input)
      }

      val response = mock[Response] // It's very hard to create a Response so use a mock.
      when(response.json).thenReturn(inputAsJson)

      val result = addressLookupService.extractFromJson(response)

      result should equal(expectedResults)
    }

    "return empty list given invalid json" in {
      val inputAsJson = Json.toJson("INVALID")

      val response = mock[Response] // It's very hard to create a Response so use a mock.
      when(response.json).thenReturn(inputAsJson)

      val result = addressLookupService.extractFromJson(response)

      result should equal(None)
    }
  }

  "fetchAddressForUprn" should {
    "return AddressViewModel when response status is 200" in {
      val service = addressServiceMock(200, Some(oSAddressbaseResultsValidDPA))

      val result = service.fetchAddressForUprn(oSAddressbaseDPA.UPRN)

      whenReady(result) {
        case Some(addressViewModel) => {
          addressViewModel.uprn.map(_.toString) should equal(Some(oSAddressbaseDPA.UPRN))
          addressViewModel.address === oSAddressbaseDPA.address
        }
        case _ => fail("Should have returned Some(AddressViewModel)")
      }
    }

    "return None when response status is not 200" in {
      val service = addressServiceMock(404, Some(oSAddressbaseResultsValidDPA))

      val result = service.fetchAddressForUprn(oSAddressbaseDPA.UPRN)

      whenReady(result) {
        _ should equal(None)
      }
    }

    "return none when response status is Ok but results is empty" in {
      val service = addressServiceMock(200, None)

      val result = service.fetchAddressForUprn(oSAddressbaseDPA.UPRN)

      whenReady(result) {
        _ should equal(None)
      }
    }

    "return none when the result has no DPA and no LPI" in {
      val service = addressServiceMock(200, Some(oSAddressbaseResultsEmptyDPAAndLPI))

      val result = service.fetchAddressForUprn(oSAddressbaseDPA.UPRN)

      whenReady(result) {
        _ should equal(None)
      }
    }

    "return none when web service throws an exception" in {
      val response = mock[Response]
      when(response.status).thenThrow(new RuntimeException("This error is generated deliberately by a test"))
      val addressLookupService = addressServiceMock(response, None)

      val result = addressLookupService.fetchAddressForUprn(oSAddressbaseDPA.UPRN)

      whenReady(result) {
        _ should equal(None)
      }
    }
  }
}
