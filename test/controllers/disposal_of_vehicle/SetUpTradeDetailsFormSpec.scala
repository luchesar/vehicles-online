package controllers.disposal_of_vehicle

import mappings.disposal_of_vehicle.SetupTradeDetails._
import helpers.disposal_of_vehicle.Helper._
import helpers.UnitSpec

class SetUpTradeDetailsFormSpec extends UnitSpec {
  "SetUpTradeDetails form" should {
    def formWithValidDefaults(traderBusinessName: String = traderBusinessNameValid,
                              traderPostcode: String = traderPostcodeValid) = {
     SetUpTradeDetails.traderLookupForm.bind(
        Map(
          dealerNameId -> traderBusinessName,
          dealerPostcodeId -> traderPostcode
        )
      )
    }

    "reject if trader business name is blank" in {
      formWithValidDefaults(traderBusinessName = "").errors should have length 2
    }

    "reject if trader business name is less than minimum length" in {
      formWithValidDefaults(traderBusinessName = "A").errors should have length 1
    }

    "reject if trader business name is more than the maximum length" in {
      formWithValidDefaults(traderBusinessName = ("A" * 101)).errors should have length 1
    }

    "accept if trader business name is valid" in {
      formWithValidDefaults(traderBusinessName = traderBusinessNameValid, traderPostcode = traderPostcodeValid).
        get.traderBusinessName should equal(traderBusinessNameValid)
    }

    "reject if trader postcode is empty" in {
      formWithValidDefaults(traderPostcode = "").errors should have length 3
    }

    "reject if trader postcode is less than the minimum length" in {
      formWithValidDefaults(traderPostcode = "M15A").errors should have length 2
    }

    "reject if trader postcode is more than the maximum length" in {
      formWithValidDefaults(traderPostcode = "SA99 1DDD").errors should have length 2
    }

    "reject if trader postcode contains special characters" in {
      formWithValidDefaults(traderPostcode = "SA99 1D$").errors should have length 1
    }

    "reject if trader postcode contains an incorrect format" in {
      formWithValidDefaults(traderPostcode = "SAR99").errors should have length 1
    }

    "accept if trader postcode is valid" in {
      formWithValidDefaults(traderBusinessName = traderBusinessNameValid, traderPostcode = traderPostcodeValid).
        get.traderPostcode should equal(traderPostcodeValid)
    }
  }
}