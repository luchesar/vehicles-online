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
import services.fakes.FakeWebServiceImpl._
import services.fakes.FakeAddressLookupService._
import play.api.http.Status._
import play.api.libs.ws.Response
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import scala.Some
import play.api.libs.ws.Response
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import scala.Some
import models.domain.disposal_of_vehicle.{PostcodeToAddressResponse, AddressViewModel}

class OSAddressLookupServiceSpec extends UnitSpec {


  def addressServiceMock(response: Future[Response]): AddressLookupService = {
    // Using the real address lookup service but passing in a fake web service that returns the responses we specify.
    new ordnance_survey.AddressLookupServiceImpl(new FakeWebServiceImpl(responseOfPostcodeWebService = response, responseOfUprnWebService = response))
  }

  def response(statusCode: Int, inputAsJson: JsValue): Future[Response] = Future {
    FakeResponse(status = statusCode, fakeJson = Some(inputAsJson))
  }

  def response(statusCode: Int,
               input: PostcodeToAddressResponse = postcodeToAddressResponseValid): Future[Response] = {
    val inputAsJson = Json.toJson(input)
    response(statusCode, inputAsJson)
  }

  "fetchAddressesForPostcode" should {
    "return seq when response status is 200 OK and returns results" in {
      val service = addressServiceMock(responseValidForOrdnanceSurvey)

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result, Timeout(Span(1, Second))) {
        r =>
          r.length should equal(postcodeToAddressResponseValid.addresses.length)
          r should equal(postcodeToAddressResponseValid.addresses.map(i => (i.uprn, i.address)))
      }
    }

    "return empty seq when response status is Ok but results is empty" in {
      val service = addressServiceMock(response(OK, PostcodeToAddressResponse(addresses = Seq.empty)))

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }
  }
}
