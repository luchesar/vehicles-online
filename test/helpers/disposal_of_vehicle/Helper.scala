package helpers.disposal_of_vehicle

import models.domain.disposal_of_vehicle.AddressViewModel

object Helper {
  val referenceNumberValid = "12345678910"
  val registrationNumberValid = "AB12AWR"
  val keeperNameValid = "John Smith"
  val postcodeValid = "CM81QJ"
  val vehicleLookupKey = referenceNumberValid + "." + registrationNumberValid

  val traderBusinessNameValid = "example trader name"
  val traderPostcodeValid = "CM81QJ"
  val traderaddressValid = "1"
  val addressWithUprn = AddressViewModel(uprn=Some(12345L),address= Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))

  val consentValid = "true"
  val mileageValid = "20000"
  val dateOfDisposalDayValid = "25"
  val dateOfDisposalMonthValid = "11"
  val dateOfDisposalYearValid = "1970"
}
