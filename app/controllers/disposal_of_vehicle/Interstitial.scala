package controllers.disposal_of_vehicle

import com.google.inject.Inject
import common.ClientSideSessionFactory
import play.api.mvc._

final class Interstitial @Inject()()(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {
  def present = Action {
    implicit request =>
      Ok(views.html.common.interstitial(nextPageUrl = routes.DisposeSuccess.present().url))
  }
}
