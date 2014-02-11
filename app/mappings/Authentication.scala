package mappings

object Authentication {
  val minLength = 6
  val maxLength = 6
  val pattern = s"\\d{$minLength,$maxLength}" // Digits only with specified size.
  val pinFormID = "PIN"
}
