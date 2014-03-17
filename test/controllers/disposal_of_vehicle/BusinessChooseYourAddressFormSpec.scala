package controllers.disposal_of_vehicle

import mappings.disposal_of_vehicle.BusinessChooseYourAddress._
import services.fakes.{FakeAddressLookupService, FakeWebServiceImpl}
import org.mockito.Mockito._
import org.mockito.Matchers._
import helpers.UnitSpec
import services.address_lookup.AddressLookupService
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import play.api.libs.ws.Response

class BusinessChooseYourAddressFormSpec extends UnitSpec {

  "BusinesssChooseYourAddress Form" should {
    def response = Future { mock[Response] }
    val addressSelectedValid = "1234"
    val fakeWebService = new FakeWebServiceImpl(response, response)
    val mockAddressLookupService = mock[AddressLookupService]
    val fakeAddressLookupService = new FakeAddressLookupService(fakeWebService)
    when(mockAddressLookupService.fetchAddressesForPostcode(anyString())).thenReturn(fakeAddressLookupService.fetchAddressesForPostcode("TEST"))
    val businessChooseYourAddress = new BusinessChooseYourAddress(mockAddressLookupService)

    def formWithValidDefaults(addressSelected: String = addressSelectedValid) = {
      businessChooseYourAddress.form.bind(
        Map(addressSelectId -> addressSelected)
      )
    }

    "accept if form is valid" in {
      formWithValidDefaults().get.uprnSelected should equal(addressSelectedValid)
    }

    "reject if addressSelect is empty" in {
      val errors = formWithValidDefaults(addressSelected = "").errors

      errors.length should equal(1)
      errors(0).key should equal(addressSelectId)
      errors(0).message should equal("error.required")
    }
  }
}
