package controllers.disposal_of_vehicle

import mappings.common.{Postcode, AddressLines, AddressAndPostcode}
import mappings.common.AddressLines._
import Postcode._
import helpers.UnitSpec
import services.fakes.FakeAddressLookupService._


class EnterAddressManuallyFormSpec extends UnitSpec {

  "EnterAddressManually Form" should {
    def formWithValidDefaults(line1: String = line1Valid,
                              line2: String = line2Valid,
                              line3: String = line3Valid,
                              line4: String = line4Valid,
                              postcode: String = postcodeValid) = {
      EnterAddressManually.form.bind(
        Map(
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line1Id" -> line1,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line2Id" -> line2,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line3Id" -> line3,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line4Id" -> line4,
          s"${AddressAndPostcode.id}.$postcodeId" -> postcode
        )
      )
    }

    "accept if form is valid with all fields filled in" in {
      val model = formWithValidDefaults().get.addressAndPostcodeModel

      model.addressLinesModel.line1 should equal(line1Valid)
      model.addressLinesModel.line2 should equal(Some(line2Valid))
      model.addressLinesModel.line3 should equal(Some(line3Valid))
      model.addressLinesModel.line4 should equal(Some(line4Valid))
      model.postcode should equal(postcodeValid)
    }

    "accept if form is valid with only mandatory filled in" in {
      val model = formWithValidDefaults(line2 = "", line3 = "").get.addressAndPostcodeModel

      model.addressLinesModel.line1 should equal(line1Valid)
      model.postcode should equal(postcodeValid)
    }


    "accept if form address lines contain hyphens" in {
      val model = formWithValidDefaults(
        line1 = "1-1",
        line2 = "address line - 2",
        line3 = "address line - 3",
        line4 = "address line - 4").get.addressAndPostcodeModel

      model.addressLinesModel.line1 should equal("1-1")
      model.addressLinesModel.line2 should equal(Some("address line - 2"))
      model.addressLinesModel.line3 should equal(Some("address line - 3"))
      model.addressLinesModel.line4 should equal(Some("address line - 4"))
    }

    "reject if line1 is blank" in {
      formWithValidDefaults(line1 = "").errors should have length 2
    }

    "reject if line1 is more than max length" in {
      formWithValidDefaults(line1 = "a" * (lineMaxLength + 1), line2 = "", line3 = "", line4 = "").errors should have length 1
    }

    "reject if line1 is greater than max length" in {
      formWithValidDefaults(line1 = "a" * (line1MaxLength + 1)).errors should have length 1
    }

    "reject if line1 contains special characters" in {
      formWithValidDefaults(line1 = "The*House").errors should have length 1
    }

    "reject if line2 is more than max length" in {
      formWithValidDefaults(line2 = "a" * (lineMaxLength + 1), line3 = "", line4 = "").errors should have length 1
    }

    "reject if line3 is more than max length" in {
      formWithValidDefaults(line2 = "", line3 = "a" * (lineMaxLength + 1), line4 = "").errors should have length 1
    }

    "reject if line4 is more than max length" in {
      formWithValidDefaults(line2 = "", line3 = "", line4 = "a" * (lineMaxLength + 1)).errors should have length 1
    }

    "reject if postcode is blank" in {
      formWithValidDefaults(postcode = "").errors should have length 3
    }

    "reject if postcode is less than min length" in {
      formWithValidDefaults(postcode = "SA99").errors should have length 2
    }

    "reject if postcode contains special characters" in {
      formWithValidDefaults(postcode = "SA99 2L$").errors should have length 1
    }

    "reject if postcode is more than max length" in {
      formWithValidDefaults(postcode = "SA99 1DDR").errors should have length 2
    }

    "reject if total length of all address lines is more than maxLengthOfLinesConcatenated" in {
      formWithValidDefaults(line1 = "a" * lineMaxLength,
        line2 = "b" * lineMaxLength,
        line3 = "c" * lineMaxLength,
        line4 = "d" * lineMaxLength
      ).errors should have length 1
    }

    "reject if html chevrons are in any line" in {
      formWithValidDefaults(line1 = "A<br>B").errors should have length 1
      formWithValidDefaults(line2 = "A<br>B").errors should have length 1
      formWithValidDefaults(line3 = "A<br>B").errors should have length 1
      formWithValidDefaults(line4 = "A<br>B").errors should have length 1
      formWithValidDefaults(postcode = "A<br>B").errors should have length 1
    }
  }
}
