package utils.helpers

import constraints.common.Required.RequiredField
import play.api.data.{Form, FormError}

import scala.language.implicitConversions

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

}