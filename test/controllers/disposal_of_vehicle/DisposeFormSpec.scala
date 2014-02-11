package controllers.disposal_of_vehicle

import org.scalatest.{Matchers, WordSpec}

class DisposeFormSpec extends WordSpec with Matchers {
  "Dispose Form" should {

    val consentValid = "true"
    val mileageValid = "20000"
    val dateOfDisposalDayValid = "25"
    val dateOfDisposalMonthValid = "11"
    val dateOfDisposalYearValid = "1970"

    def disposeFormFiller(consent: String, mileage: String, day: String, month: String, year: String) = {
      Dispose.disposeForm.bind(
        Map(
          "consent" -> consent,
          "mileage" -> mileage,
          "dateOfDisposal.day" -> day,
          "dateOfDisposal.month" -> month,
          "dateOfDisposal.year" -> year
        )
      )
    }

    "reject if consent is not ticked" in {
      disposeFormFiller(consent = "false", mileage = mileageValid, day = dateOfDisposalDayValid, month = dateOfDisposalMonthValid, year = dateOfDisposalYearValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }




//    /*Test v5cReferenceNumber*/
//    "reject if v5cReferenceNumber is blank" in {
//      v5cSearchFiller(v5cReferenceNumber = "", v5cRegistrationNumber = vehicleVRNValid, v5cPostcode = v5cPostcodeValid).fold(
//        formWithErrors => {
//          formWithErrors.errors.length should equal(3)
//        },
//        f => fail("An error should occur")
//      )
//    }
//
//    "reject if v5cReferenceNumber is less than minimum" in {
//      v5cSearchFiller(v5cReferenceNumber = "1", v5cRegistrationNumber = vehicleVRNValid, v5cPostcode = v5cPostcodeValid).fold(
//        formWithErrors => {
//          formWithErrors.errors.length should equal(1)
//        },
//        f => fail("An error should occur")
//      )
//    }
//
//    "reject if v5cReferenceNumber is more than maximum" in {
//      v5cSearchFiller(v5cReferenceNumber = "123456789101", v5cRegistrationNumber = vehicleVRNValid, v5cPostcode = v5cPostcodeValid).fold(
//        formWithErrors => {
//          formWithErrors.errors.length should equal(1)
//        },
//        f => fail("An error should occur")
//      )
//    }
//
//    "reject if v5cReferenceNumber contains letters" in {
//      v5cSearchFiller(v5cReferenceNumber = "1234567891l", v5cRegistrationNumber = vehicleVRNValid, v5cPostcode = v5cPostcodeValid).fold(
//        formWithErrors => {
//          formWithErrors.errors.length should equal(1)
//        },
//        f => fail("An error should occur")
//      )
//    }
//
//    "reject if v5cReferenceNumber contains special charapostcodecters" in {
//      v5cSearchFiller(v5cReferenceNumber = "1234567891%", v5cRegistrationNumber = vehicleVRNValid, v5cPostcode = v5cPostcodeValid).fold(
//        formWithErrors => {
//          formWithErrors.errors.length should equal(1)
//        },
//        f => fail("An error should occur")
//      )
//    }
//
//    "accept if v5cReferenceNumber contains valid input" in {
//      v5cSearchFiller(v5cReferenceNumber=v5cReferenceNumberValid,v5cRegistrationNumber=vehicleVRNValid, v5cPostcode = v5cPostcodeValid).fold(
//        formWithErrors => {fail("An error should occur")
//        },
//        f =>
//          f.v5cReferenceNumber should equal(v5cReferenceNumberValid)
//      )
//    }
//
//    /*Test VRN*/
//    "reject if vehicleVRN is blank" in {
//      v5cSearchFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = "", v5cPostcode = v5cPostcodeValid).fold(
//        formWithErrors => {
//          formWithErrors.errors.length should equal(2)
//          //errors for required field and min length
//        },
//        f => fail("An error should occur")
//      )
//    }
//
//    "reject if vehicleVRN is less than minimun" in {
//      v5cSearchFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = "a", v5cPostcode = v5cPostcodeValid).fold(
//        formWithErrors => {
//          formWithErrors.errors.length should equal(1)
//          //errors for regex and min length
//        },
//        f => fail("An error should occur")
//      )
//    }
//
//    "reject if vehicleVRN is more than maximum" in {
//      v5cSearchFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = "AB55CMWE", v5cPostcode = v5cPostcodeValid).fold(
//        formWithErrors => {
//          formWithErrors.errors.length should equal(1)
//          //errors for regex and max length
//        },
//        f => fail("An error should occur")
//      )
//    }
//    "reject if vehicleVRN contains special characters" in {
//      v5cSearchFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = "%^", v5cPostcode = v5cPostcodeValid).fold(
//        formWithErrors => {
//          formWithErrors.errors.length should equal(1)
//          //error for regex
//        },
//        f => fail("An error should occur")
//      )
//    }
//
//    "accept if vehicleVRN contains valid input" in {
//      v5cSearchFiller(v5cReferenceNumber=v5cReferenceNumberValid,v5cRegistrationNumber=vehicleVRNValid, v5cPostcode = v5cPostcodeValid).fold(
//        formWithErrors => {fail("An error should occur")
//        },
//        f =>
//          f.v5cReferenceNumber should equal(v5cReferenceNumberValid)
//      )
//    }
  }
}