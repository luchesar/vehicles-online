package controllers.disposal_of_vehicle

import org.scalatest.{Matchers, WordSpec}
import mappings.disposal_of_vehicle.BusinessAddressSelect._

class BusinessChooseYourAddressFormSpec extends WordSpec with Matchers {
  "BusinesssChooseYourAddress Form" should {
    val addressSelectedValid = address1.toViewFormat()

    def chooseYourAddressFiller(addressSelected: String = addressSelectedValid) = {
      BusinessChooseYourAddress.form.bind(
        Map(
          addressSelectId -> addressSelected
        )
      )
    }

    "accept if form is valid" in {
      chooseYourAddressFiller().fold(
        formWithErrors => fail("An error should occur"),
        f => {
          f.addressSelected should equal(addressSelectedValid)
        }
      )
    }

    "reject if form is blank" in {
      chooseYourAddressFiller(addressSelected = "").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)

          formWithErrors.errors(0).key should equal(addressSelectId)
          formWithErrors.errors(0).message should equal("error.required")
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
