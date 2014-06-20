package common

object LogFormats {
  // TODO add unit tests for this. Especially values 0, 1, 4, 8, 9
  def anonymize(inputString: String): String = {
    val anonymousChar = "*"
    val charIndex =
      if (inputString.length == 0) 0
      else if (inputString.length > 8) 4
      else inputString.length / 2
    anonymousChar * (inputString.length - charIndex) + inputString.takeRight(charIndex)
  }
}
