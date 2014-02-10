package controllers.disposal_of_vehicle

import app.DisposalOfVehicle
import org.scalatest.{Matchers, WordSpec}
import app.DisposalOfVehicle.BusinessAddressSelect._

class BusinessChooseYourAddressFormSpec extends WordSpec with Matchers {
  "BusinesssChooseYourAddress Form" should {
    val businessNameValid = "DVLA"
    val addressSelectedValid = "1"

    def chooseYourAddressFiller(businessName: String= businessNameValid, addressSelected: String = addressSelectedValid) = {
      BusinessChooseYourAddress.businessChooseYourAddressForm.bind(
        Map(
          businessNameId -> businessName,
          addressSelectId -> addressSelected
        )
      )
    }

    "accept if form is valid" in {
      chooseYourAddressFiller().fold(
        formWithErrors => fail("An error should occur"),
        f => {
          f.businessName should equal(businessNameValid)
          f.addressSelected should equal(addressSelectedValid)
        }
      )
    }

    "reject if form is blank" in {
      chooseYourAddressFiller(businessName = "", addressSelected = "").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(3)
          formWithErrors.errors(0).key should equal(businessNameId)
          formWithErrors.errors(0).message should equal("error.minLength")

          formWithErrors.errors(1).key should equal(businessNameId)
          formWithErrors.errors(1).message should equal("error.required")

          formWithErrors.errors(2).key should equal(addressSelectId)
          formWithErrors.errors(2).message should equal("error.required")
        },
        f => fail("An error should occur")
      )
    }

    "reject if businessName is more than max length" in {
      chooseYourAddressFiller(businessName = "1234567890123456789012345678901234567890123456789012345678901234567890").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
          formWithErrors.errors(0).key should equal(businessNameId)
          formWithErrors.errors(0).message should equal("error.maxLength")
        },
        f => fail("An error should occur")
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

    "reject if addressSelected has invalid mapping" in {
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
