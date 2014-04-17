package services.address_lookup.ordnance_survey

import services.fakes.FakeWebServiceImpl
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import services.address_lookup._
import org.mockito.Mockito._
import services.address_lookup.ordnance_survey.domain._
import play.api.libs.json.Json
import java.net.URI
import play.api.libs.json.JsValue
import helpers.UnitSpec
import services.fakes.FakeResponse
import org.scalatest.time.Span
import org.scalatest.time.Second
import services.fakes.FakeWebServiceImpl.{uprnAddressPairWithDefaults, traderUprnValid}
import services.fakes.FakeAddressLookupService._
import play.api.http.Status._
import play.api.libs.ws.Response
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import scala.Some

class OSAddressLookupServiceSpec extends UnitSpec {
  // TODO Re-write so that it now calls a micro-service
  /*
  val oSAddressbaseResultsValidDPA = {
    val result = OSAddressbaseResult(DPA = Some(uprnAddressPairWithDefaults()), LPI = None)
    Seq(result, result, result)
  }

  val oSAddressbaseResultsEmptyDPAAndLPI = {
    val result = OSAddressbaseResult(DPA = None, LPI = None)
    Seq(result, result, result)
  }

  def addressServiceMock(response: Future[Response], results: Option[Seq[OSAddressbaseResult]]): AddressLookupService = {
    // Using the real address lookup service but passing in a fake web service that returns the responses we specify.
    new ordnance_survey.AddressLookupServiceImpl(new FakeWebServiceImpl(responseOfPostcodeWebService = response, responseOfUprnWebService = response))
  }

  def response(statusCode: Int, inputAsJson: JsValue): Future[Response] = Future {
    FakeResponse(status = statusCode, fakeJson = Some(inputAsJson))
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
    "return seq when response status is 200 OK and returns results" in {
      val service = addressServiceMock(response(OK), Some(oSAddressbaseResultsValidDPA))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result, Timeout(Span(1, Second))) {
        r =>
          r.length should equal(oSAddressbaseResultsValidDPA.length)
          r should equal(oSAddressbaseResultsValidDPA.map(i => (i.DPA.get.UPRN, i.DPA.get.address)))
      }
    }

    "return empty seq when response status is Ok but results is empty" in {
      val input = OSAddressbaseSearchResponse(header = header, results = None)
      val service = addressServiceMock(response(OK, input), None)

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return empty seq when response status is not 200 OK" in {
      val service = addressServiceMock(response(NOT_FOUND), Some(oSAddressbaseResultsValidDPA))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return empty seq when the result has no DPA and no LPI" in {
      val input = OSAddressbaseSearchResponse(header = header, results = None)
      val service = addressServiceMock(response(OK, input), Some(oSAddressbaseResultsEmptyDPAAndLPI))

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
      val service = addressServiceMock(response(OK, inputAsJson), Some(oSAddressbaseResultsValidDPA))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "not throw when an address contains a building number that contains letters" in {
      val expected = Seq(
        (traderUprnValid.toString, s"presentationProperty AAA, 123A, property stub, street stub, town stub, area stub, $postcodeValid"),
        (traderUprnValid.toString, s"presentationProperty BBB, 123B, property stub, street stub, town stub, area stub, $postcodeValid"),
        (traderUprnValid.toString, s"presentationProperty stub, 789C, property stub, street stub, town stub, area stub, $postcodeValid")
      )
      val dpa1 = OSAddressbaseResult(DPA = Some(uprnAddressPairWithDefaults(houseNumber = "789C")), LPI = None)
      val dpa2 = OSAddressbaseResult(DPA = Some(uprnAddressPairWithDefaults(houseName = "presentationProperty BBB", houseNumber = "123B")), LPI = None)
      val dpa3 = OSAddressbaseResult(DPA = Some(uprnAddressPairWithDefaults(houseName = "presentationProperty AAA", houseNumber = "123A")), LPI = None)
      val oSAddressbaseResultsValidDPA = Seq(dpa1, dpa2, dpa3)

      val input = OSAddressbaseSearchResponse(header = header, results = Some(oSAddressbaseResultsValidDPA))
      val service = addressServiceMock(response(OK, input), Some(oSAddressbaseResultsValidDPA))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        r =>
          r.length should equal(oSAddressbaseResultsValidDPA.length)
          r shouldBe expected
      }
    }

    "return seq of (uprn, address) sorted by building number then building name" in {
      val expected = Seq(
        (traderUprnValid.toString, s"presentationProperty AAA, 123, property stub, street stub, town stub, area stub, $postcodeValid"),
        (traderUprnValid.toString, s"presentationProperty BBB, 123, property stub, street stub, town stub, area stub, $postcodeValid"),
        (traderUprnValid.toString, s"presentationProperty stub, 789, property stub, street stub, town stub, area stub, $postcodeValid")
      )
      val dpa1 = OSAddressbaseResult(DPA = Some(uprnAddressPairWithDefaults(houseNumber = "789")), LPI = None)
      val dpa2 = OSAddressbaseResult(DPA = Some(uprnAddressPairWithDefaults(houseName = "presentationProperty BBB", houseNumber = "123")), LPI = None)
      val dpa3 = OSAddressbaseResult(DPA = Some(uprnAddressPairWithDefaults(houseName = "presentationProperty AAA", houseNumber = "123")), LPI = None)
      val oSAddressbaseResultsValidDPA = Seq(dpa1, dpa2, dpa3)

      val input = OSAddressbaseSearchResponse(header = header, results = Some(oSAddressbaseResultsValidDPA))
      val service = addressServiceMock(response(OK, input), Some(oSAddressbaseResultsValidDPA))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        r =>
          r.length should equal(oSAddressbaseResultsValidDPA.length)
          r shouldBe expected
      }
    }
  }

  "fetchAddressForUprn" should {
    "return AddressViewModel when response status is 200 OK" in {
      val service = addressServiceMock(response(OK), Some(oSAddressbaseResultsValidDPA))

      val result = service.fetchAddressForUprn(uprnAddressPairWithDefaults().UPRN)

      whenReady(result) {
        case Some(addressViewModel) =>
          addressViewModel.uprn.map(_.toString) should equal(Some(uprnAddressPairWithDefaults().UPRN))
          addressViewModel.address === uprnAddressPairWithDefaults().address
        case _ => fail("Should have returned Some(AddressViewModel)")
      }
    }

    "return None when response status is not 200 OK" in {
      val service = addressServiceMock(response(NOT_FOUND), Some(oSAddressbaseResultsValidDPA))

      val result = service.fetchAddressForUprn(uprnAddressPairWithDefaults().UPRN)

      whenReady(result) {
        _ should equal(None)
      }
    }

    "return none when response status is 200 OK but results is empty" in {
      val input = OSAddressbaseSearchResponse(header = header, results = None)
      val service = addressServiceMock(response(OK, input), None)

      val result = service.fetchAddressForUprn(uprnAddressPairWithDefaults().UPRN)

      whenReady(result) {
        _ should equal(None)
      }
    }

    "return none when the result has no DPA and no LPI" in {
      val input = OSAddressbaseSearchResponse(header = header, results = Some(oSAddressbaseResultsEmptyDPAAndLPI))
      val service = addressServiceMock(response(OK, input), Some(oSAddressbaseResultsEmptyDPAAndLPI))

      val result = service.fetchAddressForUprn(uprnAddressPairWithDefaults().UPRN)

      whenReady(result) {
        _ should equal(None)
      }
    }

    "return none when web service throws an exception" in {
      val addressLookupService = addressServiceMock(responseThrows, None)

      val result = addressLookupService.fetchAddressForUprn(uprnAddressPairWithDefaults().UPRN)

      whenReady(result) {
        _ should equal(None)
      }
    }
  }*/
}
