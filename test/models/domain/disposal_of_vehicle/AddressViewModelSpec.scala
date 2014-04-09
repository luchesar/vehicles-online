package models.domain.disposal_of_vehicle

import services.fakes.FakeVehicleLookupWebService._
import helpers.UnitSpec

class AddressViewModelSpec extends UnitSpec {

  "AddressViewModel - model" should {

    "handle a uprn of the correct size" in {
      val address = AddressViewModel(uprn = Some(keeperUprnValid), address = Seq("line1", "line2", "line2"))

      val actualUprn = address.uprn.get
      actualUprn should equal(keeperUprnValid)
      address.address.size should equal(3)
    }
  }
}