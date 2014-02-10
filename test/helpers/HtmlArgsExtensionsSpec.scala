package helpers

import org.scalatest.{Matchers, WordSpec}
import play.api.test.WithApplication
import utils.helpers.HtmlArgsExtensions.RichHtmlArgs

class HtmlArgsExtensionsSpec extends WordSpec with Matchers {
  "HtmlArgsExtensions maxlength rules" should {
    val htmlArgsMinimal: Map[Symbol, Any] = Map('title -> "test")
    val htmlArgsWithMaxLength: Map[Symbol, Any] = Map('title -> "test", 'maxLength -> 60)

    "remove maxLength from args when key maxLength is present and in test mode" in new WithApplication {
      // Arrange
      val richHtmlArgs = new RichHtmlArgs(htmlArgsWithMaxLength)

      // Act
      val result = richHtmlArgs.maxLengthRules

      // Assert
      result should equal(htmlArgsMinimal)
    }

    "return the same args when key maxLength is present and in test mode" in new WithApplication {
      // Arrange
      val richHtmlArgs = new RichHtmlArgs(htmlArgsWithMaxLength, validationOff = false)

      // Act
      val result = richHtmlArgs.maxLengthRules

      // Assert
      result should equal(htmlArgsWithMaxLength)
    }

    "return the same args when key maxLength is not present and test mode" in new WithApplication {
      // Arrange
      val richHtmlArgs = new RichHtmlArgs(htmlArgsMinimal) // Override validationOff to check the behaviour of the production code.

      // Act
      val result = richHtmlArgs.maxLengthRules

      // Assert
      result should equal(htmlArgsMinimal)
    }

    "add key maxLength with default value to args not present and not in test mode" in new WithApplication {
      // Arrange
      val richHtmlArgs = new RichHtmlArgs(htmlArgsMinimal, validationOff = false) // Override validationOff to check the behaviour of the production code.

      // Act
      val result = richHtmlArgs.maxLengthRules

      // Assert
      result should equal(htmlArgsWithMaxLength)
    }
  }

  "HtmlArgsExtensions autocomplete rules" should {
    val htmlArgsMinimal: Map[Symbol, Any] = Map('title -> "test")
    val htmlArgsWithAutoCompleteOff: Map[Symbol, Any] = Map('title -> "test", 'autocomplete -> "off")
    val htmlArgsWithAutoCompleteOn: Map[Symbol, Any] = Map('title -> "test", 'autocomplete -> "on")

    "add autocomplete off when key is not present" in new WithApplication {
      // Arrange
      val richHtmlArgs = new RichHtmlArgs(htmlArgsMinimal)

      // Act
      val result = richHtmlArgs.autoCompleteRules

      // Assert
      result should equal(htmlArgsWithAutoCompleteOff)
    }

    "return the same args when key-value autocomplete 'off' is present" in new WithApplication {
      // Arrange
      val richHtmlArgs = new RichHtmlArgs(htmlArgsWithAutoCompleteOff)

      // Act
      val result = richHtmlArgs.autoCompleteRules

      // Assert
      result should equal(htmlArgsWithAutoCompleteOff)
    }

    "replace key-value autocomplete 'on' with autocomplete 'off'" in new WithApplication {
      // Arrange
      val richHtmlArgs = new RichHtmlArgs(htmlArgsWithAutoCompleteOn)

      // Act
      val result = richHtmlArgs.autoCompleteRules

      // Assert
      result should equal(htmlArgsWithAutoCompleteOff)
    }
  }
}
