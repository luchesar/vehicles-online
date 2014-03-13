package services.address_lookup.gds

import org.scalatest.{Matchers, WordSpec}
import scala.io.Source
import java.net.URI
import play.api.libs.json._
import helpers.disposal_of_vehicle.PostcodePage._
import org.scalatest.WordSpec
import services.fakes.FakeWebServiceImpl
import org.scalatest.mock.MockitoSugar
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import services.address_lookup.ordnance_survey.AddressLookupServiceImpl
import helpers.disposal_of_vehicle.PostcodePage.postcodeValid
import org.mockito.Mockito._
import services.address_lookup.ordnance_survey.domain._
import play.api.libs.json.Json
import java.net.URI
import play.api.libs.ws.Response
import org.scalatest._
import org.scalatest.concurrent._

class GdsPostcodeLookupSpec extends WordSpec with Matchers {
  "fetchAddressesForPostcode" should {
    /*
    The service will:
    1) Send postcode string to GDS micro-service
    2) Get a response from the GDS micro-service
    3) Translate the response into a Seq that can be used by the drop-down
    */
    "return empty seq when cannot connect to micro-service" in {
      val service = addressServiceMock(404, None)

      val result = service.fetchAddressesForPostcode(postcodeValid)

      whenReady(result) { _ shouldBe empty }

      pending
    }

    "return empty seq when micro-service throws" in {
      pending
    }

    "return empty seq when micro-service returns empty seq (meaning no addresses found)" in {
      pending
    }

    "return empty seq when micro-service returns invalid JSON" in {
      pending
    }

    "return seq when micro-service returns list of addresses" in {
      pending
    }
  }
}
