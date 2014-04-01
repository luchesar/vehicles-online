package controllers.disposal_of_vehicle

import mappings.disposal_of_vehicle.VehicleLookup._
import helpers.disposal_of_vehicle.Helper._
import org.mockito.Mockito._
import org.mockito.Matchers._
import models.domain.disposal_of_vehicle.{VehicleDetailsRequest, VehicleLookupFormModel}
import services.fakes.FakeVehicleLookupService
import controllers.disposal_of_vehicle
import helpers.UnitSpec

class VehicleLookupFormSpec extends UnitSpec {

  "Vehicle lookup form" should {
    val mockVehicleDetailsRequest = mock[VehicleDetailsRequest]
    val mockWebService = mock[services.VehicleLookupService]
    when(mockWebService.invoke(any[VehicleDetailsRequest])).thenReturn(new FakeVehicleLookupService().invoke(mockVehicleDetailsRequest))
    val vehicleLookup = new disposal_of_vehicle.VehicleLookup(mockWebService)

    def formWithValidDefaults(referenceNumber: String = referenceNumberValid,
                              registrationNumber: String = registrationNumberValid,
                              consent: String = consentValid) = {
      vehicleLookup.vehicleLookupForm.bind(
        Map(
          referenceNumberId -> referenceNumber,
          registrationNumberId -> registrationNumber,
          consentId -> consent
        )
      )
    }

    /** *********************
      * referenceNumber tests
      */
    "reject if referenceNumber is blank" in {
      formWithValidDefaults(referenceNumber = "").errors should have length 3
    }

    "reject if referenceNumber is less than min length" in {
      formWithValidDefaults(referenceNumber = "1234567891").errors should have length 1
    }

    "reject if referenceNumber is greater than max length" in {
      formWithValidDefaults(referenceNumber = "123456789101").errors should have length 1
    }

    "reject if referenceNumber contains letters" in {
      formWithValidDefaults(referenceNumber = "qwertyuiopl").errors should have length 1
    }

    "reject if referenceNumber contains special characters" in {
      formWithValidDefaults(referenceNumber = "£££££££££££").errors should have length 1
    }

    "accept if referenceNumber is valid" in {
      formWithValidDefaults(registrationNumber = registrationNumberValid).get.referenceNumber should equal(referenceNumberValid)
    }

    /** ************************
      * registrationNumber tests
      */

    "reject if registrationNumber is empty" in {
      formWithValidDefaults(registrationNumber = "").errors should have length 2
    }

    "reject if registrationNumber is less than min length" in {
      formWithValidDefaults(registrationNumber = "a").errors should have length 2
    }

    "reject if registrationNumber is more than max length" in {
      formWithValidDefaults(registrationNumber = "AB53WERT").errors should have length 1
    }

    "reject if registrationNumber contains special characters" in {
      formWithValidDefaults(registrationNumber = "ab53ab%").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format AA" in {
      formWithValidDefaults(registrationNumber = "AA").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format 11" in {
      formWithValidDefaults(registrationNumber = "11").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format AAA" in {
      formWithValidDefaults(registrationNumber = "AAA").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format A1A" in {
      formWithValidDefaults(registrationNumber = "A1A").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format AAAA" in {
      formWithValidDefaults(registrationNumber = "AAAA").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format 1111" in {
      formWithValidDefaults(registrationNumber = "1111").errors should have length 1
    }


    "reject if registrationNumber is in an incorrect format AAAAA" in {
      formWithValidDefaults(registrationNumber = "AAAAA").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format 1AAAA" in {
      formWithValidDefaults(registrationNumber = "1AAAA").errors should have length 1
    }


    "reject if registrationNumber is in an incorrect format 11111" in {
      formWithValidDefaults(registrationNumber = "11111").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format A99999" in {
      formWithValidDefaults(registrationNumber = "A99999").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format AAAA99" in {
      formWithValidDefaults(registrationNumber = "AAAA99").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format AAAAA9" in {
      formWithValidDefaults(registrationNumber = "AAAAA9").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format " in {
      formWithValidDefaults(registrationNumber = "AAAAAAA").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format 1AAAAA" in {
      formWithValidDefaults(registrationNumber = "1AAAAA").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format 11AAAA" in {
      formWithValidDefaults(registrationNumber = "11AAAA").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format 11111A" in {
      formWithValidDefaults(registrationNumber = "11111A").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format 111111" in {
      formWithValidDefaults(registrationNumber = "111111").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format AA999999" in {
      formWithValidDefaults(registrationNumber = "AA999999").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format A9999999" in {
      formWithValidDefaults(registrationNumber = "A9999999").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format A999A999" in {
      formWithValidDefaults(registrationNumber = "A999A9999").errors should have length 1
    }


    "reject if registrationNumber is in an incorrect format A99999A9" in {
      formWithValidDefaults(registrationNumber = "A99999A9").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format AAAAAAAA" in {
      formWithValidDefaults(registrationNumber = "AAAAAAAA").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format A1AAAAAA" in {
      formWithValidDefaults(registrationNumber = "A1AAAAAA").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format AA1AAAAA" in {
      formWithValidDefaults(registrationNumber = "AA1AAAAA").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format AAA1AAAA" in {
      formWithValidDefaults(registrationNumber = "AAA1AAAA").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format AAAA1AAA" in {
      formWithValidDefaults(registrationNumber = "AAAA1AAA").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format AAAAA1AA" in {
      formWithValidDefaults(registrationNumber = "AAAAA1AA").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format AAAAAA1A" in {
      formWithValidDefaults(registrationNumber = "AAAAAA1A").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format AAAAAAA1" in {
      formWithValidDefaults(registrationNumber = "AAAAAAA1").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format AAAAAA11" in {
      formWithValidDefaults(registrationNumber = "AAAAAA11").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format AAAAA11A" in {
      formWithValidDefaults(registrationNumber = "AAAAA11A").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format AAAA11AA" in {
      formWithValidDefaults(registrationNumber = "AAAA11AA").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format AA11AAAA" in {
      formWithValidDefaults(registrationNumber = "AA11AAAA").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format A11AAAAA" in {
      formWithValidDefaults(registrationNumber = "A11AAAAA").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format 11AAAAAA" in {
      formWithValidDefaults(registrationNumber = "11AAAAAA").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format 111AAAAA" in {
      formWithValidDefaults(registrationNumber = "111AAAAA").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format A111AAAA" in {
      formWithValidDefaults(registrationNumber = "A111AAAA").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format AAAA111A" in {
      formWithValidDefaults(registrationNumber = "AAAA111A").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format AAAAA111" in {
      formWithValidDefaults(registrationNumber = "AAAAA111").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format AAAA1111" in {
      formWithValidDefaults(registrationNumber = "AAAA1111").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format 11111AAA" in {
      formWithValidDefaults(registrationNumber = "11111AAA").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format 111111AA" in {
      formWithValidDefaults(registrationNumber = "11111AA").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format 1111111A" in {
      formWithValidDefaults(registrationNumber = "111111A").errors should have length 1
    }

    "reject if registrationNumber is in an incorrect format 11111111" in {
      formWithValidDefaults(registrationNumber = "1111111").errors should have length 1
    }


    "reject if registrationNumber is in an incorrect format 1111AAAA" in {
      formWithValidDefaults(registrationNumber = "1111AAAA").errors should have length 1
    }

    "accept if registrationNumber equals A9" in {
      formWithValidDefaults(registrationNumber = "A9").get.registrationNumber should equal("A9")
    }

    "accept if registrationNumber equals 9A" in {
      formWithValidDefaults(registrationNumber = "9A").get.registrationNumber should equal("9A")
    }

    "accept if registrationNumber equals AA9" in {
      formWithValidDefaults(registrationNumber = "AA9").get.registrationNumber should equal("AA9")
    }

    "accept if registrationNumber equals A99" in {
      formWithValidDefaults(registrationNumber = "A99").get.registrationNumber should equal("A99")
    }

    "accept if registrationNumber equals 9AA" in {
      formWithValidDefaults(registrationNumber = "9AA").get.registrationNumber should equal("9AA")
    }

    "accept if registrationNumber equals 99A" in {
      formWithValidDefaults(registrationNumber = "99A").get.registrationNumber should equal("99A")
    }

    "accept if registrationNumber equals AAA9" in {
      formWithValidDefaults(registrationNumber = "AAA9").get.registrationNumber should equal("AAA9")
    }

    "accept if registrationNumber equals A999" in {
      formWithValidDefaults(registrationNumber = "A999").get.registrationNumber should equal("A999")
    }

    "accept if registrationNumber equals AA99" in {
      formWithValidDefaults(registrationNumber = "AA99").get.registrationNumber should equal("AA99")
    }

    "accept if registrationNumber equals 9AAA" in {
      formWithValidDefaults(registrationNumber = "9AAA").get.registrationNumber should equal("9AAA")
    }

    "accept if registrationNumber equals 99AA" in {
      formWithValidDefaults(registrationNumber = "99AA").get.registrationNumber should equal("99AA")
    }

    "accept if registrationNumber equals 999A" in {
      formWithValidDefaults(registrationNumber = "999A").get.registrationNumber should equal("999A")
    }

    "accept if registrationNumber equals A9AAA" in {
      formWithValidDefaults(registrationNumber = "A9AAA").get.registrationNumber should equal("A9AAA")
    }

    "accept if registrationNumber equals AAA9A" in {
      formWithValidDefaults(registrationNumber = "AAA9A").get.registrationNumber should equal("AAA9A")
    }

    "accept if registrationNumber equals AAA99" in {
      formWithValidDefaults(registrationNumber = "AAA99").get.registrationNumber should equal("AAA99")
    }

    "accept if registrationNumber equals AA999" in {
      formWithValidDefaults(registrationNumber = "AA999").get.registrationNumber should equal("AA999")
    }

    "accept if registrationNumber equals 99AAA" in {
      formWithValidDefaults(registrationNumber = "99AAA").get.registrationNumber should equal("99AAA")
    }

    "accept if registrationNumber equals 999AA" in {
      formWithValidDefaults(registrationNumber = "999AA").get.registrationNumber should equal("999AA")
    }

    "accept if registrationNumber equals 9999A" in {
      formWithValidDefaults(registrationNumber = "9999A").get.registrationNumber should equal("9999A")
    }

    "accept if registrationNumber equals A9999" in {
      formWithValidDefaults(registrationNumber = "A9999").get.registrationNumber should equal("A9999")
    }

    "accept if registrationNumber equals A99AAA" in {
      formWithValidDefaults(registrationNumber = "A99AAA").get.registrationNumber should equal("A99AAA")
    }

    "accept if registrationNumber equals AAA99A" in {
      formWithValidDefaults(registrationNumber = "AAA99A").get.registrationNumber should equal("AAA99A")
    }

    "accept if registrationNumber equals AAA999" in {
      formWithValidDefaults(registrationNumber = "AAA999").get.registrationNumber should equal("AAA999")
    }

    "accept if registrationNumber equals 999AAA" in {
      formWithValidDefaults(registrationNumber = "999AAA").get.registrationNumber should equal("999AAA")
    }

    "accept if registrationNumber equals AA9999" in {
      formWithValidDefaults(registrationNumber = "AA9999").get.registrationNumber should equal("AA9999")
    }

    "accept if registrationNumber equals 9999AA" in {
      formWithValidDefaults(registrationNumber = "9999AA").get.registrationNumber should equal("9999AA")
    }

    "accept if registrationNumber equals A999AAA" in {
      formWithValidDefaults(registrationNumber = "A999AAA").get.registrationNumber should equal("A999AAA")
    }

    "accept if registrationNumber equals AAA999A" in {
      formWithValidDefaults(registrationNumber = "AAA999A").get.registrationNumber should equal("AAA999A")
    }

    "accept if registrationNumber equals AAA9999 (NI format)" in {
      formWithValidDefaults(registrationNumber = "AAA9999").get.registrationNumber should equal("AAA9999")
    }

    "accept if registrationNumber equals AA99AAA" in {
      formWithValidDefaults(registrationNumber = "AA99AAA").get.registrationNumber should equal("AA99AAA")
    }

    "accept if registrationNumber equals 9999AAA" in {
      formWithValidDefaults(registrationNumber = "9999AAA").get.registrationNumber should equal("9999AAA")
    }

    /** *************
      * consent tests
      */
    "reject if consent is not ticked" in {
      formWithValidDefaults(consent = "").errors should have length 1
    }

  }
}