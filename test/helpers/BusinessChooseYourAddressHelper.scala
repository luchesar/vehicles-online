package helpers

import play.api.test.TestBrowser
import models.domain.change_of_address.{V5cSearchConfirmationModel, LoginConfirmationModel}
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito

object BusinessChooseYourAddressHelper extends WordSpec with Matchers with Mockito {
  val businessNameValid = "DVLA"
  val addressValid = "1"

  def businessChooseYourAddressPopulate(browser: TestBrowser, businessName: String = businessNameValid) = {
    import app.DisposalOfVehicle.BusinessAddressSelect._
    browser.goTo("/disposal-of-vehicle/business-choose-your-address")
    browser.fill(s"#${businessNameId}") `with` businessName
    browser.click(s"#${addressSelectId} option[value='${FirstAddress}']")
    browser.submit("button[type='submit']")
  }
}
