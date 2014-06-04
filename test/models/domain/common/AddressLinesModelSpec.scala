package models.domain.common

import helpers.UnitSpec
import services.fakes.FakeAddressLookupService._

final class AddressLinesModelSpec extends UnitSpec {
  "toViewFormat" should {
    "return all lines when all lines are set to a value" in {
      AddressLinesModel(buildingNameOrNumber = BuildingNameOrNumberValid,
        line2 = Some(Line2Valid),
        line3 = Some(Line3Valid),
        line4 = Line4Valid).toViewFormat should equal(Seq(BuildingNameOrNumberValid, Line2Valid, Line3Valid, Line4Valid))
    }

    "remove unset fields so there are no gaps" in {
      AddressLinesModel(buildingNameOrNumber = BuildingNameOrNumberValid,
        line4 = Line4Valid).toViewFormat should equal(Seq(BuildingNameOrNumberValid, Line4Valid))
    }
  }

  "totalCharacters" should {
    "return zero for empty fields" in {
      AddressLinesModel(buildingNameOrNumber = "", line4 = "").totalCharacters should equal(0)
    }

    "return expected length when only mandatory fields are filled" in {
      AddressLinesModel(buildingNameOrNumber = BuildingNameOrNumberValid, line4 = Line4Valid).totalCharacters should equal(BuildingNameOrNumberValid.length + Line4Valid.length)
    }

    "return expected length when some fields are not filled" in {
      AddressLinesModel(buildingNameOrNumber = BuildingNameOrNumberValid,
        line2 = None,
        line3 = None,
        line4 = Line4Valid).totalCharacters should equal(BuildingNameOrNumberValid.length + Line4Valid.length)
    }

    "return expected length when all fields are filled" in {
      AddressLinesModel(buildingNameOrNumber = BuildingNameOrNumberValid,
        line2 = Some(Line2Valid),
        line3 = Some(Line3Valid),
        line4 = Line4Valid).totalCharacters should equal(BuildingNameOrNumberValid.length + Line2Valid.length + Line3Valid.length + Line4Valid.length)
    }
  }
}
