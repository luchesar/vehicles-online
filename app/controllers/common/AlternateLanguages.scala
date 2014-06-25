package controllers.common

import play.api.Play.current
import play.api.i18n.Lang
import play.api.mvc._

object AlternateLanguages extends Controller {

  def withLanguage(chosenLanguage: String) = Action { implicit request =>
    Redirect(request.headers.get("Referer").getOrElse("No Referer in header")).
      withLang(Lang(chosenLanguage))
  }

}