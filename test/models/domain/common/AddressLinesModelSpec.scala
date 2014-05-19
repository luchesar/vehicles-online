package models.domain.common

import helpers.UnitSpec
import services.fakes.FakeAddressLookupService._

class AddressLinesModelSpec extends UnitSpec {
  "toViewFormat" should {
    "return all lines when all lines are set to a value" in {
      AddressLinesModel(line1 = line1Valid,
        line2 = Some(line2Valid),
        line3 = Some(line3Valid),
        line4 = Some(line4Valid)).toViewFormat should equal(Seq(line1Valid, line2Valid, line3Valid, line4Valid))
    }

    "remove unset fields so there are no gaps" in {
      AddressLinesModel(line1 = line1Valid,
        line4 = Some(line4Valid)).toViewFormat should equal(Seq(line1Valid, line4Valid))
    }
  }

  "totalCharacters" should {
    "return zero for empty fields" in {
      AddressLinesModel(line1 = "").totalCharacters should equal(0)
    }

    "return expected length when only mandatory fields are filled" in {
      AddressLinesModel(line1 = line1Valid).totalCharacters should equal(line1Valid.length)
    }

    "return expected length when some fields are not filled" in {
      AddressLinesModel(line1 = line1Valid,
        line2 = None,
        line3 = None,
        line4 = Some(line4Valid)).totalCharacters should equal(line1Valid.length + line4Valid.length)
    }

    "return expected length when all fields are filled" in {
      AddressLinesModel(line1 = line1Valid,
        line2 = Some(line2Valid),
        line3 = Some(line3Valid),
        line4 = Some(line4Valid)).totalCharacters should equal(line1Valid.length + line2Valid.length + line3Valid.length + line4Valid.length)
    }
  }
}
