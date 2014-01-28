package controllers.change_of_address

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class AuthenticationIntegrationSpec extends Specification with Tags {

  val PINValid = "123456"

  "Authentication Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/authentication")

      // Assert
      titleMustContain("authentication")

    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      // Arrange
      browser.goTo("/authentication")

      // Act
      browser.fill("#PIN") `with` PINValid
      browser.submit("button[type='submit']")

      // Assert
      titleMustEqual("Change of keeper - retrieve a vehicle record")
    }
  }
}

//ToDo convert test from specs2 to scalatest
/*class AuthenticationIntegrationSpec extends WordSpec with Matchers with HtmlUnit {
  val host = "http://localhost:9000/"

  "Authentication Integration" should {
    "be presented" in {
      // Arrange & Act
      go to (host + "authentication")


      println(pageSource )
      // Assert
      pageTitle should equal ("Change of keeper - authentication")
    }

    "go to next page after the button is clicked" in {
      // Arrange
      val validPIN = "123456"
      go to (host + "authentication")
      textField("#PIN").value = validPIN

      // Act
      click on id("submit")

      // Assert
      pageTitle should equal ("Change of keeper - retrieve a vehicle record")
    }
  }
}*/


