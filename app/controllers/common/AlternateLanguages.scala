package controllers.common

import play.api.Play.current
import play.api.i18n.Lang
import play.api.mvc.{Action, Controller}

object AlternateLanguages extends Controller {

  def withLanguage(chosenLanguage: String) = Action { implicit request =>
    Redirect(request.headers.get(REFERER).getOrElse("No Referer in header")).
      withLang(Lang(chosenLanguage))
  }
}