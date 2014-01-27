package controllers.change_of_address

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import org.scalatest.{Matchers, WordSpec}


@RunWith(classOf[JUnitRunner])
class V5cSearchFormSpec extends WordSpec with Matchers {
  "V5cSearch Form" should {

    val V5cReferenceNumberNID = "V5cReferenceNumber"
    val V5cReferenceNumberValid = "12345678910"
    val vehicleVRNID = "VehicleRegistrationNumber"
    val vehicleVRNValid = "a1"

    def v5cSearchFiller(V5cReferenceNumber: String,vehicleVRN: String ) = {
      V5cSearch.v5cSearchForm.bind(
        Map(
          V5cReferenceNumberNID -> V5cReferenceNumber,
          vehicleVRNID-> vehicleVRN
        )
      )
    }
    /*Test V5cReferenceNumber*/
    "reject if V5cReferenceNumber is blank" in {
      v5cSearchFiller(V5cReferenceNumber = "", vehicleVRN = vehicleVRNValid  ).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(3)
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if V5cReferenceNumber is less than minimun" in {
      v5cSearchFiller(V5cReferenceNumber = "1", vehicleVRN = vehicleVRNValid  ).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if V5cReferenceNumber is more than maximum" in {
      v5cSearchFiller(V5cReferenceNumber = "123456789101", vehicleVRN = vehicleVRNValid  ).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if V5cReferenceNumber contains letters" in {
      v5cSearchFiller(V5cReferenceNumber = "1234567891l", vehicleVRN = vehicleVRNValid  ).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if V5cReferenceNumber contains special characters" in {
      v5cSearchFiller(V5cReferenceNumber = "1234567891%", vehicleVRN = vehicleVRNValid  ).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "accept if V5cReferenceNumber contains valid input" in {
      v5cSearchFiller(V5cReferenceNumber=V5cReferenceNumberValid,vehicleVRN=vehicleVRNValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f =>
          f.V5cReferenceNumber should equal(V5cReferenceNumberValid)
      )
    }

    /*Test VRN*/
    "reject if vehicleVRN is blank" in {
      v5cSearchFiller(V5cReferenceNumber = V5cReferenceNumberValid, vehicleVRN = "").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
          //errors for required field and min length
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if vehicleVRN is less than minimun" in {
      v5cSearchFiller(V5cReferenceNumber = V5cReferenceNumberValid, vehicleVRN = "a").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
          //errors for regex and min length
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if vehicleVRN is more than maximum" in {
      v5cSearchFiller(V5cReferenceNumber = V5cReferenceNumberValid, vehicleVRN = "AB55CMWE").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
          //errors for regex and max length
        },
        f => "An error should occur" should equal("Valid")
      )
    }
    "reject if vehicleVRN contains special characters" in {
      v5cSearchFiller(V5cReferenceNumber = V5cReferenceNumberValid, vehicleVRN = "%^").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
          //error for regex
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if vehicleVRN input not in correct format" in {
      v5cSearchFiller(V5cReferenceNumber = V5cReferenceNumberValid, vehicleVRN = "ABCDF12").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
          //error for regex
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "accept if vehicleVRN contains valid input" in {
      v5cSearchFiller(V5cReferenceNumber=V5cReferenceNumberValid,vehicleVRN=vehicleVRNValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f =>
          f.V5cReferenceNumber should equal(V5cReferenceNumberValid)
      )
    }
  }
}