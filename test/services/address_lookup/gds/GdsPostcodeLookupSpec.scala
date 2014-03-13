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


  def addressServiceMockResponse(webServiceResponse: Future[Response]): AddressLookupService = {
    // This class allows overriding of the base classes methods which call the real web service.
    class PartialMockAddressLookupService(ws: services.WebService = new FakeWebServiceImpl,
                                          responseOfPostcodeWebService: Future[Response] = Future {
                                            mock[Response]
                                          },
                                          responseOfUprnWebService: Future[Response] = Future {
                                            mock[Response]
                                          }) extends gds.AddressLookupServiceImpl(ws) {

      override protected def callPostcodeWebService(postcode: String): Future[Response] = responseOfPostcodeWebService

      override protected def callUprnWebService(uprn: String): Future[Response] = responseOfUprnWebService
    }

    new PartialMockAddressLookupService(
      responseOfPostcodeWebService = webServiceResponse,
      responseOfUprnWebService = webServiceResponse)
  }

  // Wrap simple response status code in a Response (mock).
  def addressServiceMock(statusCode: Int): AddressLookupService = {
    val response = mock[Response]
    when(response.status).thenReturn(statusCode)
    addressServiceMockResponse(
      Future {
        response
      }
    )
  }

  val addressValid: Address =
    Address(
      gssCode = "gssCode stub",
      countryCode = "countryCode stub",
      postcode = "postcode stub",
      houseName = Some("houseName stub"),
      houseNumber = Some("houseNumber stub"),
      presentation = Presentation(property = Some("property stub"),
        street = Some("street stub"),
        town = Some("town stub"),
        area = Some("area stub"),
        postcode = "postcode stub",
        uprn = "uprn stub"),
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

  "fetchAddressesForPostcode" should {
    "return empty seq when cannot connect to micro-service" in {
      val service = addressServiceMock(404)

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return empty seq when micro-service throws" in {
      val response = mock[Response]
      when(response.status).thenThrow(new RuntimeException("This error is generated deliberately by a test"))
      val service = addressServiceMockResponse(
        webServiceResponse = Future {
          response
        }
      )

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return empty seq when micro-service returns invalid JSON" in {
      val inputAsJson = Json.toJson("INVALID")
      val response = mock[Response]
      when(response.status).thenReturn(200)
      when(response.json).thenReturn(inputAsJson)
      val service = addressServiceMockResponse(
        webServiceResponse = Future {
          response
        })

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return empty seq when micro-service returns empty seq (meaning no addresses found)" in {
      val expectedResults: Seq[Address] = Seq.empty
      val inputAsJson = Json.toJson(expectedResults)
      val response = mock[Response]
      when(response.status).thenReturn(200)
      when(response.json).thenReturn(inputAsJson)
      val service = addressServiceMockResponse(
        webServiceResponse = Future {
          response
        })

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return seq of (uprn, address) when micro-service returns a single address" in {
      val expected = ("uprn stub", "property stub, street stub, town stub, area stub, postcode stub")
      val input: Seq[Address] = Seq(addressValid)
      val inputAsJson = Json.toJson(input)
      val response = mock[Response]
      when(response.status).thenReturn(200)
      when(response.json).thenReturn(inputAsJson)
      val service = addressServiceMockResponse(
        webServiceResponse = Future {
          response
        })

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe Seq(expected)
      }
    }

    "return seq of (uprn, address) when micro-service returns many addresses" in {
      val expected = ("uprn stub", "property stub, street stub, town stub, area stub, postcode stub")
      val input = Seq(addressValid, addressValid, addressValid)
      val inputAsJson = Json.toJson(input)
      val response = mock[Response]
      when(response.status).thenReturn(200)
      when(response.json).thenReturn(inputAsJson)
      val service = addressServiceMockResponse(
        webServiceResponse = Future {
          response
        })

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe Seq(expected, expected, expected)
      }
    }
  }

  "fetchAddressForUprn" should {
    "return empty seq when cannot connect to micro-service" in {
      pending
    }
    "return empty seq when micro-service throws" in {
      pending
    }
    "return empty seq when micro-service returns invalid JSON" in {
      pending
    }
    "return empty seq when micro-service returns empty seq (meaning no addresses found)" in {
      pending
    }
    "return AddressViewModel when micro-service returns a single address" in {
      pending
    }
    "return AddressViewModel of the first in the seq when micro-service returns a many addresses" in {
      pending
    }
  }
}
