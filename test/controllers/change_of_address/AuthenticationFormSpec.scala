package controllers.change_of_address

import app.ChangeOfAddress.Authentication._
import org.scalatest.{Matchers, WordSpec}

class AuthenticationFormSpec extends WordSpec with Matchers {
  "Authentication Form" should {
    val pinValid = "123456"

    def authenticationFiller(pin: String) = {
      Authentication.authenticationForm.bind(
        Map(
          pinFormID -> pin
        )
      )
    }

    "reject when pin is empty" in {
      authenticationFiller(pin="").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(3)
          //errors for min length, regex, required
        },
        f => fail("An error should occur")
      )
    }

    "reject when pin contains letters" in {
      authenticationFiller(pin="abcdef").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
          //error for regex
        },
        f => fail("An error should occur")
      )
    }

    "reject when pin contains special characters" in {
      authenticationFiller(pin="£").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
          //errors for regex and min length
        },
        f => fail("An error should occur")
      )
    }

    "reject when pin is less than min length" in {
      authenticationFiller(pin="12345").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1) //error for min length
        },
        f => fail("An error should occur")
      )
    }

    "reject when pin is more than max length" in {
      authenticationFiller(pin="1234567").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
          //error for max length
        },
        f => fail("An error should occur")
      )
    }

    "accept when pin is valid" in {
      authenticationFiller(pin=pinValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.PIN should equal(pinValid)
      )
    }
  }
}