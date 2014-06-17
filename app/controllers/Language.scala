package controllers

import play.api.i18n.Lang
import play.api.mvc._
import play.api.Play.current

object Language extends Controller {
  def withLanguage(chosenLanguage: String) = Action { implicit request =>
    def redirectUrl(request: Request[AnyContent]): String = {
      def refererAndHost(request: Request[AnyContent]) = {
        request.headers.get("Referer").getOrElse("No Referer in header") -> request.headers.get("Host").getOrElse("No Host in header")
      }

      val (referer, host) = refererAndHost(request)
      //referer.substring(referer.indexOf(host) + host.length) // TODO Carers were doing this line instead of the below, so we should check if the below behaves on a production box.
      referer
    }

    Redirect(redirectUrl(request)).
      withLang(Lang(chosenLanguage))
  }
}
