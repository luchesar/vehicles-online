package services.address_lookup

import services.address_lookup.ordnance_survey.AddressLookupServiceImpl
import org.scalatest.mock.MockitoSugar
import org.scalatest._
import org.scalatest.concurrent._
import services.fakes.FakeWebServiceImpl

class AddressLookupSpec extends WordSpec with ScalaFutures with Matchers with MockitoSugar {
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
}
