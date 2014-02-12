package mappings.disposal_of_vehicle

import models.domain.common.Address

object BusinessAddressSelect {
  val addressSelectId = "disposal_businessChooseYourAddress_addressSelect"
  val address1 = Address(line1 = "a", line2 = Some("b"), line3 = Some("c"), line4 = Some("d"), postCode = "e")
  val address2 = Address(line1 = "q", line2 = Some("w"), line3 = Some("e"), line4 = Some("r"), postCode = "t")
}
