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
import services.address_lookup.gds.domain.Address
import services.AddressLookupService

class GdsPostcodeLookupSpec extends WordSpec with ScalaFutures with Matchers with MockitoSugar {
  "fetchAddressesForPostcode" should {
    /*
    The service will:
    1) Send postcode string to GDS micro-service
    2) Get a response from the GDS micro-service
    3) Translate the response into a Seq that can be used by the drop-down
    */


    def addressServiceMockResponse(response: Future[Response], results: Option[Seq[Address]]): AddressLookupService = {
      // This class allows overriding of the base classes methods which call the real web service.
      class PartialMockAddressLookupService(ws: services.WebService = new FakeWebServiceImpl,
                                            responseOfPostcodeWebService: Future[Response] = Future {
                                              mock[Response]
                                            },
                                            responseOfUprnWebService: Future[Response] = Future {
                                              mock[Response]
                                            },
                                            results: Option[Seq[Address]]) extends gds.AddressLookupServiceImpl(ws) {

        override protected def callPostcodeWebService(postcode: String): Future[Response] = responseOfPostcodeWebService

        override protected def callUprnWebService(uprn: String): Future[Response] = responseOfUprnWebService

        override def extractFromJson(resp: Response): Option[Seq[Address]] = results
      }

      new PartialMockAddressLookupService(
        responseOfPostcodeWebService = response,
        responseOfUprnWebService = response,
        results = results)
    }

    // Wrap simple response status code in a Response (mock).
    def addressServiceMock(statusCode: Int, results: Option[Seq[Address]]): AddressLookupService = {
      val response = mock[Response]
      when(response.status).thenReturn(statusCode)
      addressServiceMockResponse(Future {
        response
      }, results)
    }

    "return empty seq when cannot connect to micro-service" in {
      val service = addressServiceMock(404, None)

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return empty seq when micro-service throws" in {
      val response = mock[Response]
      when(response.status).thenThrow(new RuntimeException("This error is generated deliberately by a test"))
      val service = addressServiceMockResponse(Future {response}, None)

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) {
        _ shouldBe empty
      }
    }

    "return empty seq when micro-service returns empty seq (meaning no addresses found)" in {
      val service = addressServiceMock(200, None)

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
}
