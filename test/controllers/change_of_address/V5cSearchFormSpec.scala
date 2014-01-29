package controllers.change_of_address

import org.scalatest.{Matchers, WordSpec}
import app.ChangeOfAddress._

class V5cSearchFormSpec extends WordSpec with Matchers {
  "V5cSearch Form" should {

    val V5cReferenceNumberValid = "12345678910"
    val vehicleVRNValid = "a1"

    def v5cSearchFiller(V5cReferenceNumber: String,V5CRegistrationNumber: String ) = {
      V5cSearch.v5cSearchForm.bind(
        Map(
          V5cReferenceNumberNID -> V5cReferenceNumber,
          V5CRegistrationNumberID-> V5CRegistrationNumber
        )
      )
    }
    /*Test V5cReferenceNumber*/
    "reject if V5cReferenceNumber is blank" in {
      v5cSearchFiller(V5cReferenceNumber = "", V5CRegistrationNumber = vehicleVRNValid  ).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(3)
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if V5cReferenceNumber is less than minimun" in {
      v5cSearchFiller(V5cReferenceNumber = "1", V5CRegistrationNumber = vehicleVRNValid  ).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if V5cReferenceNumber is more than maximum" in {
      v5cSearchFiller(V5cReferenceNumber = "123456789101", V5CRegistrationNumber = vehicleVRNValid  ).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if V5cReferenceNumber contains letters" in {
      v5cSearchFiller(V5cReferenceNumber = "1234567891l", V5CRegistrationNumber = vehicleVRNValid  ).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if V5cReferenceNumber contains special characters" in {
      v5cSearchFiller(V5cReferenceNumber = "1234567891%", V5CRegistrationNumber = vehicleVRNValid  ).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "accept if V5cReferenceNumber contains valid input" in {
      v5cSearchFiller(V5cReferenceNumber=V5cReferenceNumberValid,V5CRegistrationNumber=vehicleVRNValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f =>
          f.v5cReferenceNumber should equal(V5cReferenceNumberValid)
      )
    }

    /*Test VRN*/
    "reject if vehicleVRN is blank" in {
      v5cSearchFiller(V5cReferenceNumber = V5cReferenceNumberValid, V5CRegistrationNumber = "").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
          //errors for required field and min length
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if vehicleVRN is less than minimun" in {
      v5cSearchFiller(V5cReferenceNumber = V5cReferenceNumberValid, V5CRegistrationNumber = "a").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
          //errors for regex and min length
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if vehicleVRN is more than maximum" in {
      v5cSearchFiller(V5cReferenceNumber = V5cReferenceNumberValid, V5CRegistrationNumber = "AB55CMWE").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
          //errors for regex and max length
        },
        f => "An error should occur" should equal("Valid")
      )
    }
    "reject if vehicleVRN contains special characters" in {
      v5cSearchFiller(V5cReferenceNumber = V5cReferenceNumberValid, V5CRegistrationNumber = "%^").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
          //error for regex
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if vehicleVRN input not in correct format" in {
      v5cSearchFiller(V5cReferenceNumber = V5cReferenceNumberValid, V5CRegistrationNumber = "ABCDF12").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
          //error for regex
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "accept if vehicleVRN contains valid input" in {
      v5cSearchFiller(V5cReferenceNumber=V5cReferenceNumberValid,V5CRegistrationNumber=vehicleVRNValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f =>
          f.v5cReferenceNumber should equal(V5cReferenceNumberValid)
      )
    }
  }
}