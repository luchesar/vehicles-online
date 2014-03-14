package controllers.disposal_of_vehicle

import org.scalatest.{Matchers, WordSpec}
import org.scalatest.mock.MockitoSugar
import mappings.common.{Postcode, AddressLines, AddressAndPostcode}
import mappings.common.AddressLines._
import helpers.disposal_of_vehicle.Helper._
import Postcode._


class EnterAddressManuallyFormSpec extends WordSpec with Matchers with MockitoSugar {
  "EnterAddressManually Form" should {
    def addressFiller(line1: String = line1Valid,
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
      addressFiller().fold(
        formWithErrors => fail(s"These errors should not occur: ${formWithErrors.errors}"),
        f => {
          f.addressAndPostcodeModel.addressLinesModel.line1 should equal(line1Valid)
          f.addressAndPostcodeModel.addressLinesModel.line2 should equal(Some(line2Valid))
          f.addressAndPostcodeModel.addressLinesModel.line3 should equal(Some(line3Valid))
          f.addressAndPostcodeModel.addressLinesModel.line4 should equal(Some(line4Valid))
          f.addressAndPostcodeModel.postcode should equal(postcodeValid)
        }
      )
    }

    "accept if form is valid with only mandatory filled in" in {
      addressFiller(line2 = "", line3 = "").fold(
        formWithErrors => fail(s"These errors should not occur: ${formWithErrors.errors}"),
        f => {
          f.addressAndPostcodeModel.addressLinesModel.line1 should equal(line1Valid)
          f.addressAndPostcodeModel.postcode should equal(postcodeValid)
        }
      )
    }


    "accept if form contains hypthens" in {
      addressFiller().fold(
        formWithErrors => fail(s"These errors should not occur: ${formWithErrors.errors}"),
        f => {
          f.addressAndPostcodeModel.addressLinesModel.line1 should equal(line1Valid)
          f.addressAndPostcodeModel.addressLinesModel.line2 should equal(Some(line2Valid))
          f.addressAndPostcodeModel.addressLinesModel.line3 should equal(Some(line3Valid))
          f.addressAndPostcodeModel.addressLinesModel.line4 should equal(Some(line4Valid))
          f.addressAndPostcodeModel.postcode should equal(postcodeValid)
        }
      )
    }

    "reject if line1 is blank" in {
      addressFiller(line1 = "").fold(
        formWithErrors => formWithErrors.errors.length should equal(2),
        f => fail("An error should occur")
      )
    }

    "reject if line1 is more than max length" in {
      addressFiller(line1 = "a" * (lineMaxLength + 1) , line2 = "", line3 = "", line4 = "").fold(
        formWithErrors => formWithErrors.errors.length should equal(1),
        f => fail("An error should occur")
      )
    }

    "reject if line2 is more than max length" in {
      addressFiller(line2 = "a" * (lineMaxLength + 1) , line3 = "", line4 = "").fold(
        formWithErrors => formWithErrors.errors.length should equal(1),
        f => fail("An error should occur")
      )
    }

    "reject if line3 is more than max length" in {
      addressFiller(line2 = "", line3 = "a" * (lineMaxLength + 1), line4 = "").fold(
        formWithErrors => formWithErrors.errors.length should equal(1),
        f => fail("An error should occur")
      )
    }

    "reject if line4 is more than max length" in {
      addressFiller(line2 = "", line3 = "", line4 = "a" * (lineMaxLength + 1) ).fold(
        formWithErrors => formWithErrors.errors.length should equal(1),
        f => fail("An error should occur")
      )
    }

    "reject if postcode is blank" in {
      addressFiller(postcode = "").fold(
        formWithErrors => formWithErrors.errors.length should equal(3),
        f => fail("An error should occur")
      )
    }

    "reject if postcode is less than min length" in {
      addressFiller(postcode = "SA99").fold(
        formWithErrors => formWithErrors.errors.length should equal(2),
        f => fail("An error should occur")
      )
    }

    "reject if postcode contains special characters" in {
      addressFiller(postcode = "SA99 2L$").fold(
        formWithErrors => formWithErrors.errors.length should equal(1),
        f => fail("An error should occur")
      )
    }

    "reject if postcode is more than max length" in {
      addressFiller(postcode = "SA99 1DDR").fold(
        formWithErrors => formWithErrors.errors.length should equal(2),
        f => fail("An error should occur")
      )
    }

    "reject if total length of all address lines is more than maxLengthOfLinesConcatenated" in {
      addressFiller(line1 = "a" * lineMaxLength, line2 = "b" * lineMaxLength, line3 = "c" * lineMaxLength, line4 = "d" * lineMaxLength).fold(
        formWithErrors => formWithErrors.errors.length should equal(1),
        f => fail(s"An error should occur: $f")
      )
    }
  }
}
