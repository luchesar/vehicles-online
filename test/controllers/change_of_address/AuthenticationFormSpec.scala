package controllers.change_of_address

import mappings.change_of_address
import change_of_address.Authentication._
import helpers.change_of_address.Helper._
import helpers.UnitSpec

class AuthenticationFormSpec extends UnitSpec {

  "Authentication Form" should {
    def authenticationFiller(pin: String) = {
      Authentication.authenticationForm.bind(
        Map(pinFormId -> pin)
      )
    }

    "reject when pin is empty" in {
      authenticationFiller(pin = "").errors should have length 3
    }

    "reject when pin contains letters" in {
      authenticationFiller(pin = "abcdef").errors should have length 1
    }

    "reject when pin contains special characters" in {
      authenticationFiller(pin = "£").errors should have length 2
    }

    "reject when pin is less than min length" in {
      authenticationFiller(pin = "12345").errors should have length 1
    }

    "reject when pin is more than max length" in {
      authenticationFiller(pin = "1234567").errors should have length 1
    }

    "accept when pin is valid" in {
      authenticationFiller(pin = pinValid).get.PIN should equal(pinValid)
    }
  }
}