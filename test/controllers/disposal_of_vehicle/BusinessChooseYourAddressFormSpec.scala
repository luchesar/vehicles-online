package controllers.disposal_of_vehicle

import app.DisposalOfVehicle._
import org.scalatest.{Matchers, WordSpec}

class BusinessChooseYourAddressFormSpec extends WordSpec with Matchers {
  "BusinesssChooseYourAddress Form" should {
    val businessNameValid = "DVLA"

    def chooseYourAddressFiller(businessName: String) = {
      BusinessChooseYourAddress.businesssChooseYourAddressForm.bind(
        Map(
          businessNameID -> businessName
        )
      )
    }

    "accept if form is valid" in {
      chooseYourAddressFiller(businessName = "DVLA").fold(
        formWithErrors => fail("An error should occur"),
        f => f.businessName should equal(businessNameValid)
      )
    }

    "reject if form is blank" in {
      chooseYourAddressFiller(businessName = "").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
          formWithErrors.errors(0).key should equal(businessNameID)
          formWithErrors.errors(0).message should equal("error.minLength")
          formWithErrors.errors(1).message should equal("error.required")
        },
        f => fail("An error should occur")
      )
    }

    "reject if businessName is more than max length" in {
      chooseYourAddressFiller(businessName = "1234567890123456789012345678901234567890123456789012345678901234567890").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
          formWithErrors.errors(0).key should equal(businessNameID)
          formWithErrors.errors(0).message should equal("error.maxLength")
        },
        f => fail("An error should occur")
      )
    }
  }
}
