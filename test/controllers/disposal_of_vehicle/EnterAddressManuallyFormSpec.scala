package controllers.disposal_of_vehicle

import helpers.UnitSpec
import mappings.common.AddressAndPostcode._
import mappings.common.AddressLines._
import mappings.common.Postcode._
import services.fakes.FakeAddressLookupService._
import composition.TestComposition.{testInjector => injector}

final class EnterAddressManuallyFormSpec extends UnitSpec {
  "form" should {
    "accept if form is valid with all fields filled in" in {
      val model = formWithValidDefaults().get.addressAndPostcodeModel
      model.addressLinesModel.line1 should equal(line1Valid)
      println("** Line 1:" + model.addressLinesModel.line1.toString)

      model.addressLinesModel.line2 should equal(Some(line2Valid))
      model.addressLinesModel.line3 should equal(Some(line3Valid))
      model.addressLinesModel.line4 should equal(line4Valid)
      model.postcode should equal(postcodeValid)
    }

    "accept if form is valid with only mandatory filled in" in {
      val model = formWithValidDefaults(line2 = "", line3 = "").get.addressAndPostcodeModel
      model.addressLinesModel.line1 should equal(line1Valid)
      model.postcode should equal(postcodeValid)
    }
  }

  "address lines" should {
    "accept if form address lines contain hyphens" in {
      val line1Hypthens = "1-12"
      val line2Hypthens = "address line - 2"
      val line3Hypthens = "address line - 3"
      val line4Hypthens = "address line - 4"
      val model = formWithValidDefaults(
        line1 = line1Hypthens,
        line2 = line2Hypthens,
        line3 = line3Hypthens,
        line4 = line4Hypthens).get.addressAndPostcodeModel

      model.addressLinesModel.line1 should equal(line1Hypthens)
      model.addressLinesModel.line2 should equal(Some(line2Hypthens))
      model.addressLinesModel.line3 should equal(Some(line3Hypthens))
      model.addressLinesModel.line4 should equal(line4Hypthens)
    }

    "reject if line 4 is blank" in {
      formWithValidDefaults(line4 = "").errors should have length 2
    }

    "reject if line1 is blank" in {
      formWithValidDefaults(line1 = "").errors should have length 2
    }

    "reject if line1 is less than min length" in {
      formWithValidDefaults(line1 = "abc", line2 = "", line3 = "", line4 = line4Valid).errors should have length 1
    }

    "reject if line1 is more than max length" in {
      formWithValidDefaults(line1 = "a" * (LineMaxLength + 1), line2 = "", line3 = "", line4 = line4Valid).errors should have length 1
    }

    "reject if line1 is greater than max length" in {
      formWithValidDefaults(line1 = "a" * (LineMaxLength + 1)).errors should have length 1
    }

    "reject if line1 contains special characters" in {
      formWithValidDefaults(line1 = "The*House").errors should have length 1
    }

    "reject if line2 is more than max length" in {
      formWithValidDefaults(line2 = "a" * (LineMaxLength + 1), line3 = "", line4 = line4Valid).errors should have length 1
    }

    "reject if line3 is more than max length" in {
      formWithValidDefaults(line2 = "", line3 = "a" * (LineMaxLength + 1), line4 = line4Valid).errors should have length 1
    }

    "reject if line4 is more than max length" in {
      formWithValidDefaults(line2 = "", line3 = "", line4 = "a" * (LineMaxLength + 1)).errors should have length 1
    }

    "reject if line4 is less than min length" in {
      formWithValidDefaults(line2 = "", line3 = "", line4 = "ab").errors should have length 1
    }

    "reject if total length of all address lines is more than maxLengthOfLinesConcatenated" in {
      formWithValidDefaults(line1 = "a" * LineMaxLength + 1,
        line2 = "b" * LineMaxLength,
        line3 = "c" * LineMaxLength,
        line4 = "d" * LineMaxLength
      ).errors should have length 1
    }

    "reject if any line contains html chevrons" in {
      formWithValidDefaults(line1 = "A<br>B").errors should have length 1
      formWithValidDefaults(line2 = "A<br>B").errors should have length 1
      formWithValidDefaults(line3 = "A<br>B").errors should have length 1
      formWithValidDefaults(line4 = "A<br>B").errors should have length 1
    }
  }

  "postcode" should {
    "reject if blank" in {
      formWithValidDefaults(postcode = "").errors should have length 3
    }

    "reject if less than min length" in {
      formWithValidDefaults(postcode = "SA99").errors should have length 2
    }

    "reject if contains special characters" in {
      formWithValidDefaults(postcode = "SA99 2L$").errors should have length 1
    }

    "reject if more than max length" in {
      formWithValidDefaults(postcode = "SA99 1DDR").errors should have length 2
    }

    "reject if contains html chevrons" in {
      formWithValidDefaults(postcode = "A<br>B").errors should have length 1
    }
  }

  private def formWithValidDefaults(line1: String = line1Valid,
                                    line2: String = line2Valid,
                                    line3: String = line3Valid,
                                    line4: String = line4Valid,
                                    postcode: String = postcodeValid) = {
    injector.getInstance(classOf[EnterAddressManually]).form.bind(
      Map(
        s"$AddressAndPostcodeId.$AddressLinesId.$Line1Id" -> line1,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line2Id" -> line2,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line3Id" -> line3,
        s"$AddressAndPostcodeId.$AddressLinesId.$Line4Id" -> line4,
        s"$AddressAndPostcodeId.$PostcodeId" -> postcode
      )
    )
  }
}
