package mappings

object V5cReferenceNumber {
  val minLength = 11
  val maxLength = 11
  val pattern = s"\\d{$minLength,$maxLength}" // Digits only with specified size.
  val key = "V5cReferenceNumber"
}