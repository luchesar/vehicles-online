package controllers.change_of_address

import org.scalatest.{Matchers, WordSpec}
import mappings.V5cSearch._

class V5cSearchFormSpec extends WordSpec with Matchers {
  "V5cSearch Form" should {

    val v5cReferenceNumberValid = "12345678910"
    val vehicleVRNValid = "a1"
    val v5cPostcodeValid = "SA44DW"

    def v5cSearchFiller(v5cReferenceNumber: String,v5cRegistrationNumber: String, v5cPostcode: String ) = {
      VehicleSearch.vehicleSearchForm.bind(
        Map(
          v5cReferenceNumberID -> v5cReferenceNumber,
          v5cRegistrationNumberID-> v5cRegistrationNumber,
          v5cPostcodeID -> v5cPostcode
        )
      )
    }

    /*Test v5cReferenceNumber*/
    "reject if v5cReferenceNumber is blank" in {
      v5cSearchFiller(v5cReferenceNumber = "", v5cRegistrationNumber = vehicleVRNValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(3)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cReferenceNumber is less than minimum" in {
      v5cSearchFiller(v5cReferenceNumber = "1", v5cRegistrationNumber = vehicleVRNValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cReferenceNumber is more than maximum" in {
      v5cSearchFiller(v5cReferenceNumber = "123456789101", v5cRegistrationNumber = vehicleVRNValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cReferenceNumber contains letters" in {
      v5cSearchFiller(v5cReferenceNumber = "1234567891l", v5cRegistrationNumber = vehicleVRNValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cReferenceNumber contains special charapostcodecters" in {
      v5cSearchFiller(v5cReferenceNumber = "1234567891%", v5cRegistrationNumber = vehicleVRNValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "accept if v5cReferenceNumber contains valid input" in {
      v5cSearchFiller(v5cReferenceNumber=v5cReferenceNumberValid,v5cRegistrationNumber=vehicleVRNValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f =>
          f.v5cReferenceNumber should equal(v5cReferenceNumberValid)
      )
    }

    /*Test VRN*/
    "reject if vehicleVRN is blank" in {
      v5cSearchFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = "", v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
          //errors for required field and min length
        },
        f => fail("An error should occur")
      )
    }

    "reject if vehicleVRN is less than minimun" in {
      v5cSearchFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = "a", v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
          //errors for regex and min length
        },
        f => fail("An error should occur")
      )
    }

    "reject if vehicleVRN is more than maximum" in {
      v5cSearchFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = "AB55CMWE", v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
          //errors for regex and max length
        },
        f => fail("An error should occur")
      )
    }
    "reject if vehicleVRN contains special characters" in {
      v5cSearchFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = "%^", v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
          //error for regex
        },
        f => fail("An error should occur")
      )
    }

    "accept if vehicleVRN contains valid input" in {
      v5cSearchFiller(v5cReferenceNumber=v5cReferenceNumberValid,v5cRegistrationNumber=vehicleVRNValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f =>
          f.v5cReferenceNumber should equal(v5cReferenceNumberValid)
      )
    }
  }
}