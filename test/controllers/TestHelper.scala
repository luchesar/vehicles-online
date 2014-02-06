package controllers

import play.api.test.TestBrowser
import models.domain.change_of_address.{V5cSearchConfirmationModel, LoginConfirmationModel, Address}
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import play.api.Play.current

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
    browser.fill("#V5cReferenceNumber") `with` v5cReferenceNumber
    browser.fill("#V5CRegistrationNumber") `with` "A2"
    browser.fill("#Postcode") `with` "SA991BD"
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
}
