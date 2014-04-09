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
import services.fakes.FakeWebServiceImpl._
import play.api.libs.ws.Response

class BusinessChooseYourAddressFormSpec extends UnitSpec {
  "BusinesssChooseYourAddress Form" should {
    val businessChooseYourAddress = {
      def response = Future { mock[Response] }
      val fakeWebService = new FakeWebServiceImpl(response, response)
      val mockAddressLookupService = mock[AddressLookupService]
      val addressLookupService = new services.address_lookup.ordnance_survey.AddressLookupServiceImpl(fakeWebService)
      when(mockAddressLookupService.fetchAddressesForPostcode(anyString())).thenReturn(addressLookupService.fetchAddressesForPostcode("TEST"))
      new BusinessChooseYourAddress(mockAddressLookupService)
    }

    def formWithValidDefaults(addressSelected: String = uprnValid.toString) = {
      businessChooseYourAddress.form.bind(
        Map(addressSelectId -> addressSelected)
      )
    }

    "accept if form is valid" in {
      formWithValidDefaults().get.uprnSelected should equal(uprnValid)
    }

    "reject if addressSelect is empty" in {
      val errors = formWithValidDefaults(addressSelected = "").errors

      errors.length should equal(1)
      errors(0).key should equal(addressSelectId)
      errors(0).message should equal("error.number")
    }
  }
}
