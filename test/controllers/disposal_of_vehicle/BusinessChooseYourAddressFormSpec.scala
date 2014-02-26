package controllers.disposal_of_vehicle

import org.scalatest.{Matchers, WordSpec}
import mappings.disposal_of_vehicle.BusinessAddressSelect._
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers._
import services.fakes.{FakeAddressLookupService, FakeWebServiceImpl}

class BusinessChooseYourAddressFormSpec extends WordSpec with Matchers with MockitoSugar {
  "BusinesssChooseYourAddress Form" should {
    val addressSelectedValid = "1234"
    val fakeWebService = new FakeWebServiceImpl()
    val mockAddressLookupService = mock[services.AddressLookupService]
    val fakeAddressLookupService = new FakeAddressLookupService(fakeWebService)
    when(mockAddressLookupService.fetchAddressesForPostcode(anyString())).thenReturn(fakeAddressLookupService.fetchAddressesForPostcode("TEST"))
    val businessChooseYourAddress = new BusinessChooseYourAddress(mockAddressLookupService)

    def chooseYourAddressFiller(addressSelected: String = addressSelectedValid) = {
      businessChooseYourAddress.form.bind(
        Map(
          addressSelectId -> addressSelected
        )
      )
    }

    "accept if form is valid" in {
      chooseYourAddressFiller().fold(
        formWithErrors => fail(s"An error should not occur ${formWithErrors.errors}"),
        f => {
          f.uprnSelected should equal(addressSelectedValid)
        }
      )
    }

    "reject if addressSelect is empty" in {
      chooseYourAddressFiller(addressSelected = "").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
          formWithErrors.errors(0).key should equal(addressSelectId)
          formWithErrors.errors(0).message should equal("error.required")
        },
        f => fail("An error should occur")
      )
    }
  }
}
