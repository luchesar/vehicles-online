package controllers

import play.api.test.TestBrowser
import models.domain.change_of_address.{V5cSearchConfirmationModel, LoginConfirmationModel}
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import play.api.Play.current
import models.domain.common.Address

object TestHelper extends WordSpec with Matchers with Mockito {

  def loginPagePopulate(browser: TestBrowser) = {
    browser.goTo("/login-page")
    browser.fill("#username") `with` "roger"
    browser.fill("#password") `with` "examplepassword"
    browser.submit("button[type='submit']")
  }

  def authenticationPopulate(browser: TestBrowser) = {
    browser.goTo("/authentication")
    browser.fill("#PIN") `with` "123456"
    browser.submit("button[type='submit']")
  }

  def v5cSearchPagePopulate(browser: TestBrowser, v5cReferenceNumber: String = "12345678910") = {
    browser.goTo("/v5c-search")
    browser.fill(s"#${app.ChangeOfAddress.v5cReferenceNumberID}") `with` v5cReferenceNumber
    browser.fill(s"#${app.ChangeOfAddress.v5cRegistrationNumberID}") `with` "A2"
    browser.fill(s"#${app.ChangeOfAddress.v5cPostcodeID}") `with` "SA991BD"
    browser.submit("button[type='submit']")
  }

  def loginCachePopulate() = {
    val address = mock[Address]
    address.line1 returns "mock line1"
    address.postCode returns "mock postcode"
    val loginConfirmationModel = mock[LoginConfirmationModel]
    loginConfirmationModel.firstName returns "mock firstName"
    loginConfirmationModel.surname returns "mock surname"
    loginConfirmationModel.address returns address
    val key = Mappings.LoginConfirmationModel.key
    play.api.cache.Cache.set(key, loginConfirmationModel)
  }
  
  def v5cCachePopulate() = {
    val v5cReferenceNumberValid = "12345678910"
    val vehicleVRNValid = "a1"
    play.api.cache.Cache.set(Mappings.V5cReferenceNumber.key, v5cReferenceNumberValid)
    play.api.cache.Cache.set(Mappings.V5cRegistrationNumber.key, vehicleVRNValid)
    val v5ckey = v5cReferenceNumberValid + "." + vehicleVRNValid
    play.api.cache.Cache.set(v5ckey, V5cSearchConfirmationModel("a", "b", "c", "d", "e"))
  }

  def vehicleLookupIntegrationHelper(browser: TestBrowser, v5cReferenceNumber: String = v5cDocumentReferenceNumberValid, v5cVehicleRegistrationNumber: String = v5cVehicleRegistrationNumberValid, v5cKeeperName: String = v5cKeeperNameValid, v5cPostcode: String = v5cPostcodeValid) = {
    browser.goTo("/disposal-of-vehicle/vehicle-lookup")

    browser.fill("#v5cReferenceNumber") `with` v5cReferenceNumber
    browser.fill("#v5cRegistrationNumber") `with` v5cVehicleRegistrationNumber
    browser.fill("#v5cKeeperName") `with` v5cKeeperName
    browser.fill("#v5cPostcode") `with` v5cPostcode
    browser.submit("button[type='submit']")
  }

  val v5cDocumentReferenceNumberValid = "12345678910"
  val v5cVehicleRegistrationNumberValid = "AB12AWR"
  val v5cKeeperNameValid = "John Smith"
  val v5cPostcodeValid = "Sa991DD"

  def traderLookupIntegrationHelper(browser: TestBrowser, traderBusinessName: String = traderBusinessNameValid, traderPostcode: String = traderPostcodeValid) = {
    browser.goTo("/disposal-of-vehicle/setup-trade-details")

    browser.fill("#traderBusinessName") `with` traderBusinessName
    browser.fill("#traderPostcode") `with` traderPostcode
    browser.submit("button[type='submit']")
  }

  val traderBusinessNameValid = "example trader name"
  val traderPostcodeValid = "SA99 1DD"
}
