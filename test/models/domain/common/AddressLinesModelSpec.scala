package models.domain.common

import helpers.UnitSpec
import services.fakes.FakeAddressLookupService._

final class AddressLinesModelSpec extends UnitSpec {
  "toViewFormat" should {
    "return all lines when all lines are set to a value" in {
      AddressLinesModel(buildingNameOrNumber = BuildingNameOrNumberValid,
        line2 = Some(Line2Valid),
        line3 = Some(Line3Valid),
        postTown = postTownValid).toViewFormat should equal(Seq(BuildingNameOrNumberValid, Line2Valid, Line3Valid, postTownValid))
    }

    "remove unset fields so there are no gaps" in {
      AddressLinesModel(buildingNameOrNumber = BuildingNameOrNumberValid,
        postTown = postTownValid).toViewFormat should equal(Seq(BuildingNameOrNumberValid, postTownValid))
    }
  }

  "totalCharacters" should {
    "return zero for empty fields" in {
      AddressLinesModel(buildingNameOrNumber = "", postTown = "").totalCharacters should equal(0)
    }

    "return expected length when only mandatory fields are filled" in {
      AddressLinesModel(buildingNameOrNumber = BuildingNameOrNumberValid, postTown = postTownValid).totalCharacters should equal(BuildingNameOrNumberValid.length + postTownValid.length)
    }

    "return expected length when some fields are not filled" in {
      AddressLinesModel(buildingNameOrNumber = BuildingNameOrNumberValid,
        line2 = None,
        line3 = None,
        postTown = postTownValid).totalCharacters should equal(BuildingNameOrNumberValid.length + postTownValid.length)
    }

    "return expected length when all fields are filled" in {
      AddressLinesModel(buildingNameOrNumber = BuildingNameOrNumberValid,
        line2 = Some(Line2Valid),
        line3 = Some(Line3Valid),
        postTown = postTownValid).totalCharacters should equal(BuildingNameOrNumberValid.length + Line2Valid.length + Line3Valid.length + postTownValid.length)
    }
  }
}
