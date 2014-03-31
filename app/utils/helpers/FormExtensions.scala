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
    def replaceError(key: String, message: String, newError: FormError): Form[T] = {
      def matchingError(e: FormError) = e.key == key && e.message == message
      val oldError = form.errors.find(matchingError)
      if (oldError.isDefined) {
        val error = if (newError.args.isEmpty) FormError(newError.key,newError.message,oldError.get.args) else newError
        form.copy(errors = form.errors.filterNot(e => e.key == key && e.message == message)).withError(error)
      }
      else form
    }
  }
}