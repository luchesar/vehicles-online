package utils.helpers

import scala.language.implicitConversions
import play.api.data.{FormError, Form}
import play.api.mvc.{Request}

object FormExtensions {
  implicit def formBinding[T](form: Form[T])(implicit request: Request[_]) = new RichForm[T](form)

  // Extension method for forms.
  class RichForm[T](form: Form[T])(implicit request: Request[_]) {
    private def replaceError(newError: FormError, matcher: FormError => Boolean): Form[T] = {
      val errorToReplace = form.errors.find(matcher)
      errorToReplace match {
        case Some(n) => form.copy(errors = form.errors.filterNot(matcher)).withError(newError) // Replace the error we were looking for.
        case None => form
      }
    }

    def replaceError(key: String, newError: FormError): Form[T] = replaceError(newError, {e => e.key == key})

    def replaceError(key: String, message: String, newError: FormError): Form[T] = replaceError(newError, { e=> e.key == key && e.message == message})

    def distinctErrors: Form[T] = form.copy(errors = form.errors.distinct)
  }
}