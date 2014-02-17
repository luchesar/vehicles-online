package models.domain.disposal_of_vehicle

import models.domain.common.{MultiLineAddress}

// TODO we should copy the way carer's separates address and postcode and then wrap that in one compound object that handles mandatory postcode and one that handles optional postcode.
case class EnterAddressManuallyModel(address: MultiLineAddress, postCode: String)
