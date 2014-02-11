package controllers.disposal_of_vehicle

import org.scalatest.{Matchers, WordSpec}
import mappings.disposal_of_vehicle.SetupTradeDetails._

class SetUpTradeDetailsFormSpec extends WordSpec with Matchers {
  "SetUpTradeDetails form" should {

    val traderBusinessNameValid = "Example trader"
    val traderPostcodeValid = "SA99 1BD"

    def traderLookupFiller(traderBusinessName: String, traderPostcode: String ) = {
     SetUpTradeDetails.traderLookupForm.bind(
        Map(
          traderBusinessNameID -> traderBusinessName,
          traderPostcodeID -> traderPostcode
        )
      )
    }
//Trader business name tests
    "reject if trader business name is blank" in {
      traderLookupFiller(traderBusinessName = "", traderPostcode = traderPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
        },
        f => fail("An error should occur"))
    }

    "reject if trader business name is less than minimum length" in {
      traderLookupFiller(traderBusinessName = "A", traderPostcode = traderPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur"))
    }

    "reject if trader business name is more than the maximum length" in {
      traderLookupFiller(traderBusinessName = "qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopq", traderPostcode = traderPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur"))
    }

    "accept if trader business name is valid" in {
      traderLookupFiller(traderBusinessName = traderBusinessNameValid, traderPostcode = traderPostcodeValid).fold(
        formWithErrors => {
          fail("An error should occur")
        },
        f => f.traderBusinessName should equal(traderBusinessNameValid))
    }

    "reject if trader postcode is empty" in {
      traderLookupFiller(traderBusinessName = traderPostcodeValid, traderPostcode = "").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(3)
        },
        f => fail("An error should occur"))
    }

    "reject if trader postcode is less than the minimum length" in {
      traderLookupFiller(traderBusinessName = traderPostcodeValid, traderPostcode = "M15A").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
        },
        f => fail("An error should occur"))
    }

    "reject if trader postcode is more than the maximum length" in {
      traderLookupFiller(traderBusinessName = traderPostcodeValid, traderPostcode = "SA99 1DDD").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
        },
        f => fail("An error should occur"))
    }

    "reject if trader postcode contains special characters" in {
      traderLookupFiller(traderBusinessName = traderPostcodeValid, traderPostcode = "SA99 1D$").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur"))
    }

    "reject if trader postcode contains an incorrect format" in {
      traderLookupFiller(traderBusinessName = traderPostcodeValid, traderPostcode = "SAR99").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur"))
    }

    "accept if trader postcode is valid" in {
      traderLookupFiller(traderBusinessName = traderBusinessNameValid, traderPostcode = traderPostcodeValid).fold(
        formWithErrors => {
          fail("An error should occur")
        },
        f => f.traderPostcode should equal(traderPostcodeValid))
    }
  }
}