package utils.helpers

import controllers.Mappings
import play.api.Play
import play.api.Play.current

// TODO Need a better name for this file & object.
object HtmlArgsSanitiser {
  def maxLength(htmlArgs: Map[Symbol, Any]) = {
    htmlArgs.contains('maxLength) match {
      case true => {
        Play.isTest match {
          case true => htmlArgs - 'maxLength // Remove maxLength when testing so we can have integration tests cause server side validation errors.
          case false => htmlArgs
        }
      }
      case false => {
        Play.isTest match {
          case true => htmlArgs
          case false => htmlArgs + ('maxLength -> Mappings.sixty) // On production we should have a maxLength, so if you forgot to add one then the default is used.
        }
      }
    }
  }
}
