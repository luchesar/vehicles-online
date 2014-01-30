package controllers

import play.api.test.TestBrowser


object Formulate {
  def loginPageDetails(browser: TestBrowser) = {
    // Arrange
    browser.goTo("/login-page")

    // Act
    browser.fill("#username") `with` "roger"
    browser.fill("#password") `with` "examplepassword"

  }
}
