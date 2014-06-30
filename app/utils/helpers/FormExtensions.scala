package utils.helpers

import constraints.common.Required.RequiredField
import play.api.data.{Mapping, Form, FormError}

import scala.language.implicitConversions
import play.api.data.format.Formatter
import play.api.data.Forms._
import play.api.data.FormError
import scala.Some
import play.api.data.validation.Constraints

object FormExtensions {
  implicit def formBinding[T](form: Form[T]) = new RichForm[T](form)

  // TODO should this be updated to look like the extension method in EncryptedCookieImplicits?

  // Extension method for forms.
  class RichForm[T](form: Form[T]) {
    private def replaceError(newError: FormError, matcher: FormError => Boolean): Form[T] = {
      val errorToReplace = form.errors.find(matcher)
      errorToReplace match {
        case Some(n) => form.copy(errors = form.errors.filterNot(matcher)).withError(newError) // Replace the error we were looking for.
        case None => form
      }
    }

    def replaceError(key: String, newError: FormError): Form[T] = replaceError(newError, { e => e.key == key})

    def replaceError(key: String, message: String, newError: FormError): Form[T] = replaceError(newError, { e => e.key == key && e.message == message})

    def distinctErrors: Form[T] = form.copy(errors = form.errors.distinct)

    def anyMandatoryFields: Boolean =
      form.mapping.mappings.exists(m => m.constraints.exists(c => c.name == Some(RequiredField)))
  }

  def trimmedText(minLength: Int = 0, maxLength: Int = Int.MaxValue, additionalTrimChars: Seq[Char] = Nil): Mapping[String] = {
    val formatter = of[String](trimmedStringFormat(additionalTrimChars))

    (minLength, maxLength) match {
      case (0, Int.MaxValue) => formatter
      case (min, Int.MaxValue) => formatter verifying Constraints.minLength(min)
      case (0, max) => {println("yeah"); formatter verifying Constraints.maxLength(max)}
      case (min, max) => formatter verifying (Constraints.minLength(min), Constraints.maxLength(max))
    }
  }

  /**
    * The nonEmpty variant of TrimmedText applies the additional constraint that the text is empty
    */
  def nonEmptyTrimmedText(minLength: Int = 0, maxLength: Int = Int.MaxValue, additionalTrimChars: Seq[Char] = Nil): Mapping[String] =
    trimmedText(minLength, maxLength, additionalTrimChars) verifying Constraints.nonEmpty

  /**
   * trim function that accepts additional chars to trim.
   * Code based on Scala's trim implementation (as it should be optimized)
   */
  private def trim(value: String, trimChars: Seq[Char]): String = {
    var len: Int = value.length
    var st: Int = 0
    val `val`: Array[Char] = value.toCharArray
    while ((st < len) && ((`val`(st) <= ' ') || trimChars.contains(`val`(st)))) {
      st += 1
    }
    while ((st < len) && ((`val`(len - 1) <= ' ') || trimChars.contains(`val`(len - 1)))) {
      len -= 1
    }
    return if (((st > 0) || (len < value.length))) value.substring(st, len) else value
  }

  private def trimmedStringFormat(trimChars: Seq[Char]): Formatter[String] = new Formatter[String] {
    def bind(key: String, data: Map[String, String]) = {
      val value = data.get(key).map(trim(_, trimChars))
      value.toRight(Seq(FormError(key, "error.required", Nil)))
    }

    def unbind(key: String, value: String) = Map(key -> value)
  }


}