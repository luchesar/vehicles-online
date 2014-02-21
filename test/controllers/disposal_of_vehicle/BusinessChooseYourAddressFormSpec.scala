package controllers.disposal_of_vehicle

import org.scalatest.{Matchers, WordSpec}
import mappings.disposal_of_vehicle.BusinessAddressSelect._
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers._
import modules.TestModule.FakeAddressLookupService
import helpers.disposal_of_vehicle.BusinessChooseYourAddressPage._

class BusinessChooseYourAddressFormSpec extends WordSpec with Matchers with MockitoSugar {
  "BusinesssChooseYourAddress Form" should {
    val addressSelectedValid = "uprn1"
    val mockAddressLookupService = mock[services.AddressLookupService]
    when(mockAddressLookupService.fetchAddressesForPostcode(anyString())).thenReturn(new FakeAddressLookupService().fetchAddressesForPostcode("TEST"))
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
        formWithErrors => fail("An error should occur"),
        f => {
          f.uprnSelected should equal(addressSelectedValid)
        }
      )
    }

    "reject if addressSelect is empty" in {
      chooseYourAddressFiller(addressSelected = "").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
          formWithErrors.errors(0).key should equal(addressSelectId)
          formWithErrors.errors(0).message should equal("error.required")
          formWithErrors.errors(1).key should equal(addressSelectId)
          formWithErrors.errors(1).message should equal("error.dropDownInvalid")
        },
        f => fail("An error should occur")
      )
    }

    "reject if posted address does not exist in the drop down list entries" in {
      chooseYourAddressFiller(addressSelected = "INVALID").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
          formWithErrors.errors(0).key should equal(addressSelectId)
          formWithErrors.errors(0).message should equal("error.dropDownInvalid")
        },
        f => fail("An error should occur")
      )
    }

  }
}
