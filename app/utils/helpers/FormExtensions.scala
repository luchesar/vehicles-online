package utils.helpers

import scala.language.implicitConversions
import play.api.data.{FormError, Form}
import play.api.mvc.{AnyContent, Request}
import scala.util.{Failure, Success, Try}
import play.api.Logger

object FormExtensions {
  implicit def formBinding[T](form: Form[T])(implicit request: Request[_]) = new RichForm[T](form)

  // Extension method for forms.
  class RichForm[T](form: Form[T])(implicit request: Request[_]) {
    def replaceError(key: String, newError: FormError): Form[T] = {
      def matchingError(e: FormError) = e.key == key // Matches on key, but is not checking args.
      val errorToReplace = form.errors.find(matchingError)
      errorToReplace match {
        case Some(n) => form.copy(errors = form.errors.filterNot(matchingError)).withError(newError) // Replace the error we were looking for.
        case None => form
      }
    }

    def replaceError(key: String, message: String, newError: FormError): Form[T] = {
      def matchingError(e: FormError) = e.key == key && e.message == message // Matches on key AND message, but is not checking args.
      val errorToReplace = form.errors.find(matchingError)
      errorToReplace match {
        case Some(n) => form.copy(errors = form.errors.filterNot(matchingError)).withError(newError) // Replace the error we were looking for.
        case None => form
      }
    }

    def distinctErrors: Form[T] = form.copy(errors = form.errors.distinct)
  }
}