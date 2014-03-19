package helpers.disposal_of_vehicle

import models.domain.disposal_of_vehicle.AddressViewModel

object Helper {
  val traderBusinessNameValid = "example trader name"
  val traderaddressValid = "1"

  val line1Valid = "123"
  val line2Valid = "line-2 stub"
  val line3Valid = "line-3 stub"
  val line4Valid = "line-4 stub"
  val postcodeValid = "CM81QJ"
  val postcodeValidWithSpace = "CM8 1QJ"
  val postcodeNoResults = "SA99 1DD"

  val referenceNumberValid = "12345678910"
  val registrationNumberValid = "AB12AWR"

  val vehicleMakeValid = "make"
  val vehicleModelValid = "model"
  val keeperNameValid = "John Smith"
  val vehicleLookupKey = referenceNumberValid + "." + registrationNumberValid

  val address1 = AddressViewModel(address= Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))
  val addressWithUprn = AddressViewModel(uprn=Some(12345L),address= Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))
  
  val consentValid = "true"
  val mileageValid = "20000"
  val dateOfDisposalDayValid = "25"
  val dateOfDisposalMonthValid = "11"
  val dateOfDisposalYearValid = "1970"
  val dateOfDisposalValid = (Some(1), Some(2), Some(2014))
}
