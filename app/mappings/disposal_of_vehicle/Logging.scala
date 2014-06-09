package mappings.disposal_of_vehicle

object Logging {
  def anonymize (inputString: String) : String = {
    val charIndex = if (inputString.length > 8) 4
    else inputString.length / 2
    "*" * (inputString.length - charIndex) + inputString.takeRight(charIndex)
  }
}
