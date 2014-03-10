package controllers.disposal_of_vehicle

import org.scalatest.{Matchers, WordSpec}
import mappings.disposal_of_vehicle.VehicleLookup._
import helpers.disposal_of_vehicle.Helper._
import org.mockito.Mockito._
import org.mockito.Matchers._
import models.domain.disposal_of_vehicle.VehicleLookupFormModel
import services.fakes.FakeVehicleLookupService
import controllers.disposal_of_vehicle
import org.scalatest.mock.MockitoSugar

class VehicleLookupFormSpec extends WordSpec with Matchers with MockitoSugar{
  "Vehicle lookup form" should {
    val mockVehicleLookupFormModel = mock[VehicleLookupFormModel]
    val mockWebService = mock[services.VehicleLookupService]
    when(mockWebService.invoke(any[VehicleLookupFormModel])).thenReturn(new FakeVehicleLookupService().invoke(mockVehicleLookupFormModel))
    val vehicleLookup = new disposal_of_vehicle.VehicleLookup(mockWebService)

    def vehicleLookupFiller(referenceNumber: String, registrationNumber: String, consent: String) = {
      vehicleLookup.vehicleLookupForm.bind(
        Map(
          referenceNumberId -> referenceNumber,
          registrationNumberId -> registrationNumber,
          consentId -> consent
        )
      )
    }

    /***********************
     * referenceNumber tests
     */
    "reject if referenceNumber is blank" in {
      vehicleLookupFiller(referenceNumber = "", registrationNumber = registrationNumberValid, consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(3)
        },
        f => fail("An error should occur")
      )
    }

    "reject if referenceNumber is less than min length" in {
      vehicleLookupFiller(referenceNumber = "1234567891", registrationNumber = registrationNumberValid, consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if referenceNumber is greater than max length" in {
      vehicleLookupFiller(referenceNumber = "123456789101", registrationNumber = registrationNumberValid, consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if referenceNumber contains letters" in {
      vehicleLookupFiller(referenceNumber = "qwertyuiopl", registrationNumber = registrationNumberValid, consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if referenceNumber contains special characters" in {
      vehicleLookupFiller(referenceNumber = "£££££££££££", registrationNumber = registrationNumberValid, consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "accept if referenceNumber is valid" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = registrationNumberValid, consent = consentValid).fold(
        formWithErrors => {
          fail("An error should occur")
        },
        f => f.referenceNumber should equal(referenceNumberValid)
      )
    }

    /**************************
     * registrationNumber te
    */

      "reject if registrationNumber is empty" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is less than min length" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "a", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is more than max length" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AB53WERT", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber contains special characters" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "ab53ab%", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format AA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format 11" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "11", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format AAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format A1A" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "A1A", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format AAAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAAA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format 1111" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "1111", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }


    "reject if registrationNumber is in an incorrect format AAAAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAAAA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format 1AAAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "1AAAA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }


    "reject if registrationNumber is in an incorrect format 11111" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "11111", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format A99999" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "A99999", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format AAAA99" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAAA99", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format AAAAA9" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAAAA9", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

      "reject if registrationNumber is in an incorrect format AAAAAA" in {
        vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAAAAA", consent = consentValid).fold(
          formWithErrors => {
            formWithErrors.errors.length should equal(1)
          },
          f => fail("An error should occur")
        )
      }

    "reject if registrationNumber is in an incorrect format 1AAAAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "1AAAAA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format 11AAAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "11AAAA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format 11111A" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "11111A", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format 111111" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "111111", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

      "reject if registrationNumber is in an incorrect format AA999999" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AA999999", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format A9999999" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "A9999999", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format A999A999" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "A999A9999", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }


    "reject if registrationNumber is in an incorrect format A99999A9" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "A99999A9", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format AAAAAAAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAAAAAAA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format A1AAAAAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "A1AAAAAA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format AA1AAAAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AA1AAAAA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format AAA1AAAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAA1AAAA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format AAAA1AAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAAA1AAA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format AAAAA1AA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAAAA1AA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format AAAAAA1A" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAAAAA1A", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format AAAAAAA1" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAAAAAA1", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format AAAAAA11" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAAAAA11", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format AAAAA11A" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAAAA11A", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format AAAA11AA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAAA11AA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format AA11AAAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AA11AAAA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format A11AAAAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "A11AAAAA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format 11AAAAAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "11AAAAAA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format 111AAAAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "111AAAAA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format A111AAAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "A111AAAA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format AAAA111A" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAAA111A", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format AAAAA111" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAAAA111", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format AAAA1111" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAAA1111", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format 11111AAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "11111AAA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format 111111AA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "11111AA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format 1111111A" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "111111A", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is in an incorrect format 11111111" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "1111111", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }


    "reject if registrationNumber is in an incorrect format 1111AAAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "1111AAAA", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "accept if registrationNumber equals A9" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "A9", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("A9")
      )
    }

    "accept if registrationNumber equals 9A" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "9A", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("9A")
      )
    }

    "accept if registrationNumber equals AA9" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AA9", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AA9")
      )
    }

    "accept if registrationNumber equals A99" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "A99", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("A99")
      )
    }

    "accept if registrationNumber equals 9AA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "9AA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("9AA")
      )
    }

    "accept if registrationNumber equals 99A" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "99A", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("99A")
      )
    }

    "accept if registrationNumber equals AAA9" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAA9", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AAA9")
      )
    }

    "accept if registrationNumber equals A999" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "A999", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("A999")
      )
    }

    "accept if registrationNumber equals AA99" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AA99", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AA99")
      )
    }

    "accept if registrationNumber equals 9AAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "9AAA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("9AAA")
      )
    }

    "accept if registrationNumber equals 99AA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "99AA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("99AA")
      )
    }

    "accept if registrationNumber equals 999A" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "999A", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("999A")
      )
    }

    "accept if registrationNumber equals A9AAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "A9AAA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("A9AAA")
      )
    }

    "accept if registrationNumber equals AAA9A" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAA9A", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AAA9A")
      )
    }

    "accept if registrationNumber equals AAA99" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAA99", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AAA99")
      )
    }

    "accept if registrationNumber equals AA999" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AA999", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AA999")
      )
    }

    "accept if registrationNumber equals 99AAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "99AAA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("99AAA")
      )
    }

    "accept if registrationNumber equals 999AA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "999AA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("999AA")
      )
    }

    "accept if registrationNumber equals 9999A" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "9999A", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("9999A")
      )
    }

    "accept if registrationNumber equals A9999" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "A9999", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("A9999")
      )
    }

    "accept if registrationNumber equals A99AAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "A99AAA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("A99AAA")
      )
    }

    "accept if registrationNumber equals AAA99A" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAA99A", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AAA99A")
      )
    }

    "accept if registrationNumber equals AAA999" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAA999", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AAA999")
      )
    }

    "accept if registrationNumber equals 999AAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "999AAA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("999AAA")
      )
    }

    "accept if registrationNumber equals AA9999" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AA9999", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AA9999")
      )
    }

    "accept if registrationNumber equals 9999AA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "9999AA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("9999AA")
      )
    }

    "accept if registrationNumber equals A999AAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "A999AAA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("A999AAA")
      )
    }

    "accept if registrationNumber equals AAA999A" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAA999A", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AAA999A")
      )
    }

    "accept if registrationNumber equals AAA9999" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AAA9999", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AAA9999")
      )
    }

    "accept if registrationNumber equals AA99AAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "AA99AAA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AA99AAA")
      )
    }

    "accept if registrationNumber equals 9999AAA" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = "9999AAA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("9999AAA")
      )
    }

    /***************
     * consent tests
     */
    "reject if consent is not ticked" in {
      vehicleLookupFiller(referenceNumber = referenceNumberValid, registrationNumber = registrationNumberValid, consent = "").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

  }
}