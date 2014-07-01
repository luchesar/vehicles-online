package common

object LogFormats {

  def anonymize(input: String): String = {
    val anonymousChar = "*"
    val startOfNonAnonymizedText =
      if (input.length == 0) 0
      else if (input.length > 8) 4
      else input.length / 2
    anonymousChar * (input.length - startOfNonAnonymizedText) + input.takeRight(startOfNonAnonymizedText)
  }
}