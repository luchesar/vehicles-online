package services.address_lookup.gds

import services.fakes.FakeWebServiceImpl
import org.scalatest.mock.MockitoSugar
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import services.address_lookup.gds
import helpers.disposal_of_vehicle.PostcodePage.postcodeValid
import org.mockito.Mockito._
import play.api.libs.ws.Response
import org.scalatest._
import org.scalatest.concurrent._
import services.address_lookup.gds.domain.{Presentation, Details, Location, Address}
import services.AddressLookupService
import play.api.libs.json.Json
import services.address_lookup.gds.domain.JsonFormats.addressFormat

class GdsPostcodeLookupSpec extends WordSpec with ScalaFutures with Matchers with MockitoSugar {
  /*
    The service will:
    1) Send postcode string to GDS micro-service
    2) Get a response from the GDS micro-service
    3) Translate the response into a Seq that can be used by the drop-down
    */


  def addressServiceMockResponse(response: Future[Response], results: Seq[Address]): AddressLookupService = {
    // This class allows overriding of the base classes methods which call the real web service.
    class PartialMockAddressLookupService(ws: services.WebService = new FakeWebServiceImpl,
                                          responseOfPostcodeWebService: Future[Response] = Future {
                                            mock[Response]
                                          },
                                          responseOfUprnWebService: Future[Response] = Future {
                                            mock[Response]
                                          },
                                          results: Seq[Address]) extends gds.AddressLookupServiceImpl(ws) {

      override protected def callPostcodeWebService(postcode: String): Future[Response] = responseOfPostcodeWebService

      override protected def callUprnWebService(uprn: String): Future[Response] = responseOfUprnWebService

      override def extractFromJson(resp: Response): Seq[Address] = results
    }

    new PartialMockAddressLookupService(
      responseOfPostcodeWebService = response,
      responseOfUprnWebService = response,
      results = results)
  }

  // Wrap simple response status code in a Response (mock).
  def addressServiceMock(statusCode: Int, results: Seq[Address]): AddressLookupService = {
    val response = mock[Response]
    when(response.status).thenReturn(statusCode)
    addressServiceMockResponse(Future {
      response
    }, results)
  }

  "fetchAddressesForPostcode" should {
    "return empty seq when cannot connect to micro-service" in {
      val service = addressServiceMock(404, Seq.empty)

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return empty seq when micro-service throws" in {
      val response = mock[Response]
      when(response.status).thenThrow(new RuntimeException("This error is generated deliberately by a test"))
      val service = addressServiceMockResponse(Future {
        response
      }, Seq.empty)

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return empty seq when micro-service returns empty seq (meaning no addresses found)" in {
      val service = addressServiceMock(200, Seq.empty)

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return empty seq when micro-service returns invalid JSON" in {
      pending
    }

    "return seq when micro-service returns list of addresses" in {
      pending
    }
  }

  "extract from json" should {
    val addressLookupService = new gds.AddressLookupServiceImpl(ws = new FakeWebServiceImpl)

    "return empty seq given invalid json" in {
      val inputAsJson = Json.toJson("INVALID")
      val response = mock[Response] // It's very hard to create a Response so use a mock.
      when(response.json).thenReturn(inputAsJson)

      val result = addressLookupService.extractFromJson(response)

      result should equal(Seq.empty)
    }

    "return expected given valid json with no results" in {
      val expectedResults: Seq[Address] = Seq.empty
      val inputAsJson = Json.toJson(expectedResults)
      val response = mock[Response] // It's very hard to create a Response so use a mock.
      when(response.json).thenReturn(inputAsJson)

      val result = addressLookupService.extractFromJson(response)

      result should equal(expectedResults)
    }

    "return expected given valid json with results" in {
      val expectedResults: Seq[Address] = Seq(
        Address(
          gssCode = "a",
          countryCode = "b",
          postcode = "c",
          houseName = Some("d"),
          houseNumber = Some("e"),
          presentation = Presentation(property = Some("f"),
            street = Some("g"),
            town = Some("h"),
            area = Some("i"),
            postcode = "j",
            uprn = "k"),
          details = Details(
            usrn = "l",
            isResidential = true,
            isCommercial = true,
            isPostalAddress = true,
            classification = "m",
            state = "n",
            organisation = Some("o")
          ),
          location = Location(
            x = 1.0d,
            y = 2.0d)
        )
      )

      val inputAsJson = Json.toJson(expectedResults)
      val response = mock[Response] // It's very hard to create a Response so use a mock.
      when(response.json).thenReturn(inputAsJson)

      val result = addressLookupService.extractFromJson(response)

      result should equal(expectedResults)
    }
  }
}
