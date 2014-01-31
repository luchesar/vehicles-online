package controllers

import play.api.test.TestBrowser
import models.domain.change_of_address.{LoginConfirmationModel, Address}
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import play.api.Play.current

object Formulate extends WordSpec with Matchers with Mockito {
  def loginPageDetails(browser: TestBrowser) = {
    browser.goTo("/login-page")
    browser.fill("#username") `with` "roger"
    browser.fill("#password") `with` "examplepassword"
    browser.submit("button[type='submit']")
  }

  def v5cSearchPageDetails(browser: TestBrowser) = {
    browser.goTo("/v5c-search")
    browser.fill("#V5cReferenceNumber") `with` "12345678910"
    browser.fill("#V5CRegistrationNumber") `with` "A2"
    browser.submit("button[type='submit']")
  }

  def PopulateLoginCache() = {
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
}
