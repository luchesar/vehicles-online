package mappings.common

import scala.Some

object StripFromString {
  def stripLine(string:String, character:String)=
    string filterNot (character contains _)

  def stripLineRequiredField(inputline: String, charsNotAccepted: String) = {
    stripLine(inputline, charsNotAccepted)
  }

  def stripLineOptionalField(inputline: Option[String], charsNotAccepted: String) = {
    inputline match {
      case Some(inputline) => {
        Some(stripLine(inputline,charsNotAccepted))
      }
      case _ => None
    }
  }
}
