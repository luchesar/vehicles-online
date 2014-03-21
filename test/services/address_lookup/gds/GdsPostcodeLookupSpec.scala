package services.address_lookup.gds

import services.fakes.FakeWebServiceImpl
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import services.address_lookup.{AddressLookupService, gds}
import helpers.disposal_of_vehicle.Helper._
import org.mockito.Mockito._
import play.api.libs.ws.Response
import services.address_lookup.gds.domain.{Presentation, Details, Location, Address}
import play.api.libs.json.{JsValue, Json}
import services.address_lookup.gds.domain.JsonFormats.addressFormat
import helpers.UnitSpec

class GdsPostcodeLookupSpec extends UnitSpec {
  /*
    The service will:
    1) Send postcode string to GDS micro-service
    2) Get a response from the GDS micro-service
    3) Translate the response into a Seq that can be used by the drop-down
    */
  def addressServiceMock(response: Future[Response]): AddressLookupService = {
    // This class allows overriding of the base classes methods which call the real web service.
    class PartialAddressService(responseOfPostcodeWebService: Future[Response],
                                responseOfUprnWebService: Future[Response]
                                 ) extends gds.AddressLookupServiceImpl(new FakeWebServiceImpl(responseOfPostcodeWebService, responseOfUprnWebService)) {
    }

    new PartialAddressService(
      responseOfPostcodeWebService = response,
      responseOfUprnWebService = response)
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

  def response(statusCode: Int, inputAsJson: JsValue) = Future {
    val response = mock[Response] // It's very hard to create a Response so use a mock.
    when(response.status).thenReturn(statusCode)
    when(response.json).thenReturn(inputAsJson)
    response
  }

  def responseThrows = Future {
    val response = mock[Response]
    when(response.status).thenThrow(new RuntimeException("This error is generated deliberately by a test"))
    response
  }

  def responseTimeout = Future {
    val response = mock[Response]
    when(response.status).thenThrow(new java.util.concurrent.TimeoutException("This error is generated deliberately by a test"))
    response
  }
/*
  "fetchAddressesForPostcode" should {
    "return empty seq when cannot connect to micro-service" in {
      val service = addressServiceMock(responseTimeout)

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) { _ shouldBe empty }
    }

    "return empty seq when response throws" in {
      val service = addressServiceMock(responseThrows)

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) { _ shouldBe empty }
    }

    "return empty seq when micro-service returns invalid JSON" in {
      val inputAsJson = Json.toJson("INVALID")
      val service = addressServiceMock(response(200, inputAsJson))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) { _ shouldBe empty }
    }

    "return empty seq when micro-service response status is not 200 (OK)" in {
      val input: Seq[Address] = Seq(addressValid())
      val inputAsJson = Json.toJson(input)
      val service = addressServiceMock(response(404, inputAsJson))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) { _ shouldBe empty }
    }


    "return empty seq when micro-service returns empty seq (meaning no addresses found)" in {
      val expectedResults: Seq[Address] = Seq.empty
      val inputAsJson = Json.toJson(expectedResults)
      val service = addressServiceMock(response(200, inputAsJson))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) { _ shouldBe empty }
    }

    "return seq of (uprn, address) when micro-service returns a single address" in {
      val expected = (uprnValid, "houseName stub, 123, property stub, street stub, town stub, area stub, postcode stub")
      val input: Seq[Address] = Seq(addressValid())
      val inputAsJson = Json.toJson(input)
      val service = addressServiceMock(response(200, inputAsJson))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) { _ shouldBe Seq(expected) }
    }

    "return seq of (uprn, address) when micro-service returns many addresses" in {
      val expected = (uprnValid, "houseName stub, 123, property stub, street stub, town stub, area stub, postcode stub")
      val input = Seq(addressValid(), addressValid(), addressValid())
      val inputAsJson = Json.toJson(input)
      val service = addressServiceMock(response(200, inputAsJson))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) { _ shouldBe Seq(expected, expected, expected) }
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
      val service = addressServiceMock(response(200, inputAsJson))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) { _ shouldBe expected }
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
      val service = addressServiceMock(response(200, inputAsJson))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) { _ shouldBe expected }
    }
  }

  "fetchAddressForUprn" should {
    "return None when cannot connect to micro-service" in {
      val service = addressServiceMock(responseTimeout)

      val result = service.fetchAddressForUprn(uprnValid)

      whenReady(result) { _ shouldBe None }
    }

    "return None when response throws" in {
      val service = addressServiceMock(responseThrows)

      val result = service.fetchAddressForUprn(uprnValid)

      whenReady(result) { _ shouldBe None }
    }

    "return None when micro-service returns invalid JSON" in {
      val inputAsJson = Json.toJson("INVALID")
      val service = addressServiceMock(response(200, inputAsJson))

      val result = service.fetchAddressForUprn(uprnValid)

      whenReady(result) { _ shouldBe None }
    }

    "return None when micro-service response status is not 200 (OK)" in {
      val input: Seq[Address] = Seq(addressValid())
      val inputAsJson = Json.toJson(input)
      val service = addressServiceMock(response(404, inputAsJson))

      val result = service.fetchAddressForUprn(uprnValid)

      whenReady(result) { _ shouldBe None }
    }

    "return None when micro-service returns empty seq (meaning no addresses found)" in {
      val inputAsJson = Json.toJson(Seq.empty)
      val service = addressServiceMock(response(200, inputAsJson))

      val result = service.fetchAddressForUprn(uprnValid)

      whenReady(result) { _ shouldBe None }
    }

    "return AddressViewModel when micro-service returns a single address" in {
      val expected = Seq("houseName stub, 123, property stub, street stub, town stub, area stub, postcode stub")
      val input: Seq[Address] = Seq(addressValid())
      val inputAsJson = Json.toJson(input)
      val service = addressServiceMock(response(200, inputAsJson))

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
      val service = addressServiceMock(response(200, inputAsJson))

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
*/
}
