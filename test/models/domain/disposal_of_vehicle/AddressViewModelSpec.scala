package models.domain.disposal_of_vehicle

import services.fakes.FakeVehicleLookupWebService._
import helpers.UnitSpec
import models.domain.common.{AddressLinesModel, AddressAndPostcodeModel}
import services.fakes.FakeAddressLookupService._
import play.api.libs.json.Json
import AddressViewModel.JsonFormat

final class AddressViewModelSpec extends UnitSpec {
  "from" should {
    "translate correctly" in {
      val addressAndPostcodeModel = AddressAndPostcodeModel(addressLinesModel = AddressLinesModel(buildingNameOrNumber = Line1Valid,
        line2 = Some(Line2Valid),
        line3 = Some(Line3Valid),
        line4 = Line4Valid),
        postcode = PostcodeValid)

      val result = AddressViewModel.from(addressAndPostcodeModel)

      result.uprn should equal(None)
      result.address should equal(Seq(Line1Valid, Line2Valid, Line3Valid, Line4Valid, PostcodeValid))
    }
  }

  "format" should {
    "serialize to json" in {
      val address = AddressViewModel(uprn = Some(KeeperUprnValid), address = Seq(Line1Valid, Line2Valid, Line3Valid, Line4Valid, PostcodeValid))
      Json.toJson(address) should equal(asJson)
    }

    "deserialize from json" in {
      val fromJson =  Json.fromJson[AddressViewModel](asJson)
      val expected = AddressViewModel(uprn = Some(KeeperUprnValid), address = Seq(Line1Valid, Line2Valid, Line3Valid, Line4Valid, PostcodeValid))
      fromJson.asOpt should equal(Some(expected))
    }
  }

  private val asJson = Json.parse("""{"uprn":10123456789,"address":["1234","line2 stub","line3 stub","line4 stub","CM81QJ"]}""")
}