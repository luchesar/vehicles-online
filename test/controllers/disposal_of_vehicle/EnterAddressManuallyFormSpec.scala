package controllers.disposal_of_vehicle

import org.scalatest.{Matchers, WordSpec}
import org.scalatest.mock.MockitoSugar
import mappings.common.PostCode
import mappings.common.MultiLineAddress
import helpers.disposal_of_vehicle.EnterAddressManuallyPage._

class EnterAddressManuallyFormSpec extends WordSpec with Matchers with MockitoSugar {
  "EnterAddressManually Form" should {
    def addressFiller(line1: String = line1Valid,
                      line2: String = line2Valid,
                      line3: String = line3Valid,
                      postCode: String = postCodeValid) = {
      EnterAddressManually.form.bind(
        Map(
          s"${MultiLineAddress.id}.${MultiLineAddress.lineOneId}" -> line1,
          s"${MultiLineAddress.id}.${MultiLineAddress.lineTwoId}" -> line2,
          s"${MultiLineAddress.id}.${MultiLineAddress.lineThreeId}" -> line3,
          PostCode.key -> postCode
        )
      )
    }

    "accept if form is valid with all fields filled in" in {
      addressFiller().fold(
        formWithErrors => fail(s"These errors should not occur: ${formWithErrors.errors}"),
        f => {
          f.address.lineOne should equal(Some(line1Valid))
          f.address.lineTwo should equal(Some(line2Valid))
          f.address.lineThree should equal(Some(line3Valid))
          f.postCode should equal(postCodeValid)
        }
      )
    }

    "accept if form is valid with only mandatory filled in" in {
      addressFiller(line2 = "", line3 = "").fold(
        formWithErrors => fail(s"These errors should not occur: ${formWithErrors.errors}"),
        f => {
          f.address.lineOne should equal(Some(line1Valid))
          f.postCode should equal(postCodeValid)
        }
      )
    }

    "reject if line1 is blank" in {
      addressFiller(line1 = "", line2 = "", line3 = "").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
          formWithErrors.errors(0).key should equal(MultiLineAddress.id)
        },
        f => fail("An error should occur")
      )
    }

    "reject if postCode is blank" in {
      addressFiller(postCode = "").fold(
        formWithErrors => {
          println(formWithErrors.errors)
          formWithErrors.errors.length should equal(3)
          formWithErrors.errors(0).key should equal(PostCode.key)
          formWithErrors.errors(0).message should equal("error.minLength")
          formWithErrors.errors(1).key should equal(PostCode.key)
          formWithErrors.errors(1).message should equal("error.required")
          formWithErrors.errors(2).key should equal(PostCode.key)
          formWithErrors.errors(2).message should equal("error.restricted.validPostcode")
        },
        f => fail("An error should occur")
      )
    }
  }
}
