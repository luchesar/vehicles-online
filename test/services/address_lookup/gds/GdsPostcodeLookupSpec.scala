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
import play.api.libs.json.{JsValue, Json}
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
                                          responseOfPostcodeWebService: Future[Response] = Future { mock[Response] },
                                          responseOfUprnWebService: Future[Response] = Future { mock[Response] }
                                          ) extends gds.AddressLookupServiceImpl(ws) {

      override protected def callPostcodeWebService(postcode: String): Future[Response] = responseOfPostcodeWebService

      override protected def callUprnWebService(uprn: String): Future[Response] = responseOfUprnWebService
    }

    new PartialMockAddressLookupService(
      responseOfPostcodeWebService = webServiceResponse,
      responseOfUprnWebService = webServiceResponse)
  }

  val uprnValid = "1"

  def addressValid(houseName: String = "houseName stub", houseNumber: String = "123"): Address =
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

  def response(statusCode: Int, inputAsJson: JsValue) = {
    val response = mock[Response] // It's very hard to create a Response so use a mock.
    when(response.status).thenReturn(statusCode)
    when(response.json).thenReturn(inputAsJson)
    Future { response }
  }

  def responseThrows = {
    val response = mock[Response]
    when(response.status).thenThrow(new RuntimeException("This error is generated deliberately by a test"))
    Future { response }
  }

  def responseTimeout = {
    val response = mock[Response]
    when(response.status).thenThrow(new java.util.concurrent.TimeoutException("This error is generated deliberately by a test"))
    Future { response }
  }

  "fetchAddressesForPostcode" should {
    "return empty seq when cannot connect to micro-service" in {
      val service = addressServiceMockResponse(responseTimeout)

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return empty seq when response throws" in {
      val service = addressServiceMockResponse(responseThrows)

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return empty seq when micro-service returns invalid JSON" in {
      val inputAsJson = Json.toJson("INVALID")
      val service = addressServiceMockResponse(response(200, inputAsJson))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return empty seq when micro-service response status is not 200 (OK)" in {
      val input: Seq[Address] = Seq(addressValid())
      val inputAsJson = Json.toJson(input)
      val service = addressServiceMockResponse(response(404, inputAsJson))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }


    "return empty seq when micro-service returns empty seq (meaning no addresses found)" in {
      val expectedResults: Seq[Address] = Seq.empty
      val inputAsJson = Json.toJson(expectedResults)
      val service = addressServiceMockResponse(response(200, inputAsJson))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return seq of (uprn, address) when micro-service returns a single address" in {
      val expected = (uprnValid, "houseName stub, 123, property stub, street stub, town stub, area stub, postcode stub")
      val input: Seq[Address] = Seq(addressValid())
      val inputAsJson = Json.toJson(input)
      val service = addressServiceMockResponse(response(200, inputAsJson))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe Seq(expected)
      }
    }

    "return seq of (uprn, address) when micro-service returns many addresses" in {
      val expected = (uprnValid, "houseName stub, 123, property stub, street stub, town stub, area stub, postcode stub")
      val input = Seq(addressValid(), addressValid(), addressValid())
      val inputAsJson = Json.toJson(input)
      val service = addressServiceMockResponse(response(200, inputAsJson))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe Seq(expected, expected, expected)
      }
    }

    "not throw when an address contains a building number that contains letters" in {
      val expected = Seq(
        (uprnValid, "houseName AAA, 123A, property stub, street stub, town stub, area stub, postcode stub"),
        (uprnValid, "houseName BBB, 123B, property stub, street stub, town stub, area stub, postcode stub"),
        (uprnValid, "houseName stub, 789C, property stub, street stub, town stub, area stub, postcode stub")
      )
      val input = Seq(
        addressValid(houseNumber = "789C"),
        addressValid(houseName = "houseName BBB", houseNumber = "123B"),
        addressValid(houseName = "houseName AAA", houseNumber = "123A")
      )
      val inputAsJson = Json.toJson(input)
      val service = addressServiceMockResponse(response(200, inputAsJson))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe expected
      }
    }

    "return seq of (uprn, address) sorted by building number then building name" in {
      val expected = Seq(
        (uprnValid, "houseName AAA, 123, property stub, street stub, town stub, area stub, postcode stub"),
        (uprnValid, "houseName BBB, 123, property stub, street stub, town stub, area stub, postcode stub"),
        (uprnValid, "houseName stub, 789, property stub, street stub, town stub, area stub, postcode stub")
      )
      val input = Seq(
        addressValid(houseNumber = "789"),
        addressValid(houseName = "houseName BBB", houseNumber = "123"),
        addressValid(houseName = "houseName AAA", houseNumber = "123")
      )
      val inputAsJson = Json.toJson(input)
      val service = addressServiceMockResponse(response(200, inputAsJson))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe expected
      }
    }
  }

  "fetchAddressForUprn" should {
    "return None when cannot connect to micro-service" in {
      val service = addressServiceMockResponse(responseTimeout)

      val result = service.fetchAddressForUprn(uprnValid)

      whenReady(result) {
        _ shouldBe None
      }
    }

    "return None when response throws" in {
      val service = addressServiceMockResponse(responseThrows)

      val result = service.fetchAddressForUprn(uprnValid)

      whenReady(result) {
        _ shouldBe None
      }
    }

    "return None when micro-service returns invalid JSON" in {
      val inputAsJson = Json.toJson("INVALID")
      val service = addressServiceMockResponse(response(200, inputAsJson))

      val result = service.fetchAddressForUprn(uprnValid)

      whenReady(result) {
        _ shouldBe None
      }
    }

    "return None when micro-service response status is not 200 (OK)" in {
      val input: Seq[Address] = Seq(addressValid())
      val inputAsJson = Json.toJson(input)
      val service = addressServiceMockResponse(response(404, inputAsJson))

      val result = service.fetchAddressForUprn(uprnValid)

      whenReady(result) {
        _ shouldBe None
      }
    }

    "return None when micro-service returns empty seq (meaning no addresses found)" in {
      val inputAsJson = Json.toJson(Seq.empty)
      val service = addressServiceMockResponse(response(200, inputAsJson))

      val result = service.fetchAddressForUprn(uprnValid)

      whenReady(result) {
        _ shouldBe None
      }
    }

    "return AddressViewModel when micro-service returns a single address" in {
      val expected = Seq("houseName stub, 123, property stub, street stub, town stub, area stub, postcode stub")
      val input: Seq[Address] = Seq(addressValid())
      val inputAsJson = Json.toJson(input)
      val service = addressServiceMockResponse(response(200, inputAsJson))

      val result = service.fetchAddressForUprn(uprnValid)

      whenReady(result) {
        case Some(addressViewModel) => {
          addressViewModel.uprn should equal(Some(uprnValid.toLong))
          println("addressViewModel.address: " + addressViewModel.address)
          println("expected: " + expected)
          addressViewModel.address === expected
        }
        case _ => fail("Should have returned Some(AddressViewModel)")
      }
    }

    "return AddressViewModel of the first in the seq when micro-service returns many addresses" in {
      val expected = Seq("houseName stub, 123, property stub, street stub, town stub, area stub, postcode stub")
      val input: Seq[Address] = Seq(addressValid(), addressValid(), addressValid())
      val inputAsJson = Json.toJson(input)
      val service = addressServiceMockResponse(response(200, inputAsJson))

      val result = service.fetchAddressForUprn(uprnValid)

      whenReady(result) {
        case Some(addressViewModel) => {
          addressViewModel.uprn should equal(Some(uprnValid.toLong))
          addressViewModel.address === expected
        }
        case _ => fail("Should have returned Some(AddressViewModel)")
      }
    }
  }
}
