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
}
