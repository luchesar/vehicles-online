package controllers.change_of_address

import org.scalatest.{Matchers, WordSpec}
import app.ChangeOfAddress._

class V5cSearchFormSpec extends WordSpec with Matchers {
  "V5cSearch Form" should {

    val v5cReferenceNumberValid = "12345678910"
    val vehicleVRNValid = "a1"

    def v5cSearchFiller(v5cReferenceNumber: String,v5cRegistrationNumber: String ) = {
      V5cSearch.v5cSearchForm.bind(
        Map(
          v5cReferenceNumberID -> v5cReferenceNumber,
          v5cRegistrationNumberID-> v5cRegistrationNumber
        )
      )
    }
    /*Test v5cReferenceNumber*/
    "reject if v5cReferenceNumber is blank" in {
      v5cSearchFiller(v5cReferenceNumber = "", v5cRegistrationNumber = vehicleVRNValid  ).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(3)
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if v5cReferenceNumber is less than minimun" in {
      v5cSearchFiller(v5cReferenceNumber = "1", v5cRegistrationNumber = vehicleVRNValid  ).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if v5cReferenceNumber is more than maximum" in {
      v5cSearchFiller(v5cReferenceNumber = "123456789101", v5cRegistrationNumber = vehicleVRNValid  ).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if v5cReferenceNumber contains letters" in {
      v5cSearchFiller(v5cReferenceNumber = "1234567891l", v5cRegistrationNumber = vehicleVRNValid  ).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if v5cReferenceNumber contains special characters" in {
      v5cSearchFiller(v5cReferenceNumber = "1234567891%", v5cRegistrationNumber = vehicleVRNValid  ).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "accept if v5cReferenceNumber contains valid input" in {
      v5cSearchFiller(v5cReferenceNumber=v5cReferenceNumberValid,v5cRegistrationNumber=vehicleVRNValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f =>
          f.v5cReferenceNumber should equal(v5cReferenceNumberValid)
      )
    }

    /*Test VRN*/
    "reject if vehicleVRN is blank" in {
      v5cSearchFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = "").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
          //errors for required field and min length
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if vehicleVRN is less than minimun" in {
      v5cSearchFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = "a").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
          //errors for regex and min length
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if vehicleVRN is more than maximum" in {
      v5cSearchFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = "AB55CMWE").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
          //errors for regex and max length
        },
        f => "An error should occur" should equal("Valid")
      )
    }
    "reject if vehicleVRN contains special characters" in {
      v5cSearchFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = "%^").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
          //error for regex
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if vehicleVRN input not in correct format" in {
      v5cSearchFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = "ABCDF12").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
          //error for regex
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "accept if vehicleVRN contains valid input" in {
      v5cSearchFiller(v5cReferenceNumber=v5cReferenceNumberValid,v5cRegistrationNumber=vehicleVRNValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f =>
          f.v5cReferenceNumber should equal(v5cReferenceNumberValid)
      )
    }
  }
}