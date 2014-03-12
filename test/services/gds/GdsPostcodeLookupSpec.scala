package services.gds

import org.scalatest.{Matchers, WordSpec}
import scala.io.Source
import java.net.URI
import play.api.libs.json._

class GdsPostcodeLookupSpec extends WordSpec with Matchers {
  "fetchAddressesForPostcode" should {
    /*
    The service will:
    1) Send postcode string to GDS micro-service
    2) Get a response from the GDS micro-service
    3) Translate the response into a Seq that can be used by the drop-down
    */
    "return empty seq when cannot connect to micro-service" in {
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
