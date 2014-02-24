package controllers.disposal_of_vehicle

import org.scalatest.{Matchers, WordSpec}
import org.scalatest.mock.MockitoSugar
import mappings.common.PostCode
import helpers.disposal_of_vehicle.EnterAddressManuallyPage._
import scala.Some
import mappings.common.{AddressLines, AddressAndPostcode}
import mappings.common.AddressLines._
import helpers.disposal_of_vehicle.PostcodePage._
import mappings.disposal_of_vehicle.Postcode._

class EnterAddressManuallyFormSpec extends WordSpec with Matchers with MockitoSugar {
  "EnterAddressManually Form" should {


    def addressFiller(line1: String = line1Valid,line2: String = line2Valid,line3: String = line3Valid,line4: String = line4Valid,postcode: String = postcodeValid) = {
      EnterAddressManually.form.bind(
        Map(
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line1Id" -> line1,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line2Id" -> line2,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line3Id" -> line3,
          s"${AddressAndPostcode.id}.${AddressLines.id}.$line4Id" -> line4,
          s"${AddressAndPostcode.id}.$postcodeID" -> postcode
        )
      )
    }

    "accept if form is valid with all fields filled in" in {
      addressFiller().fold(
        formWithErrors => fail(s"These errors should not occur: ${formWithErrors.errors}"),
        f => {
          f.addressAndPostcodeModel.addressLinesModel.line1 should equal(Some(line1Valid))
          f.addressAndPostcodeModel.addressLinesModel.line2 should equal(Some(line2Valid))
          f.addressAndPostcodeModel.addressLinesModel.line3 should equal(Some(line3Valid))
          f.addressAndPostcodeModel.postcode should equal(postCodeValid)
        }
      )
    }

    "accept if form is valid with only mandatory filled in" in {
      addressFiller(line2 = "", line3 = "").fold(
        formWithErrors => fail(s"These errors should not occur: ${formWithErrors.errors}"),
        f => {
          f.addressAndPostcodeModel.addressLinesModel.line1 should equal(Some(line1Valid))
          f.addressAndPostcodeModel.postcode should equal(postCodeValid)
        }
      )
    }

    "reject if line1 is blank" in {
      addressFiller(line1 = "", line2 = "", line3 = "").fold(
        formWithErrors => formWithErrors.errors.length should equal(1),
        f => fail("An error should occur")
      )
    }

    "reject if line1 is more than max length" in {
      addressFiller(line1 = "qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwerty", line2 = "", line3 = "").fold(
        formWithErrors => formWithErrors.errors.length should equal(1),
        f => fail("An error should occur")
      )
    }

    "reject if postcode is blank" in {
      addressFiller(line2 = "", line3 = "", postcode = "").fold(
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

  }
}
