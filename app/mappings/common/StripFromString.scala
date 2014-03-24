package mappings.common

import scala.Some

object StripFromString {
  /* code below is currently not being used - it has been used to strip characters from a whole line of an address
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
  */

  def stripEndOfLineRequiredField(inputline: String): String = {
    inputline.takeRight(1) match {
      case (",") => inputline.dropRight(1)
      case (".") => inputline.dropRight(1)
      case _ => inputline
    }
  }

  def stripEndOfLineOptionalField(inputline: Option[String]): Option[String] = {
    inputline match {
      case Some(inputline) => {
        inputline.takeRight(1) match {
          case (",") => Some(inputline.dropRight(1))
          case (".") => Some(inputline.dropRight(1))
          case _ => Some(inputline)
        }
      }
      case _ => None
    }
  }
}
