package mappings.disposal_of_vehicle

import models.domain.common.Address

object BusinessAddressSelect {
  val addressSelectId = "disposal_businessChooseYourAddress_addressSelect"
  val address1 = Address(line1 = "44 Hythe Road", line2 = Some("White City"), line3 = Some("London"), line4 = None, postCode = "NW10 6RJ")
  val address2 = Address(line1 = "Penarth Road", line2 = Some("Cardiff"), line3 = None, line4 = None, postCode = "CF11 8TT")
}
