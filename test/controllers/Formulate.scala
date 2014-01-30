package controllers

import play.api.test.TestBrowser


object Formulate {
  def loginPageDetails(browser: TestBrowser) = {
    // Arrange & Act
    browser.goTo("/login-page")

    browser.fill("#username") `with` "roger"
    browser.fill("#password") `with` "examplepassword"

  }

  def v5cSearchPageDetails(browser: TestBrowser) = {
    // Arrange & Act
    val validvehicleVRN = "A2"
    val validV5cReferenceNumber = "12345678910"
    browser.goTo("/v5c-search")
    browser.fill("#V5cReferenceNumber") `with` validV5cReferenceNumber
    browser.fill("#V5CRegistrationNumber") `with` validvehicleVRN
    browser.submit("button[type='submit']")

  }
}
