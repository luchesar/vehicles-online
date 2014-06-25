package controllers.disposal_of_vehicle

import javax.inject.Inject

import common.ClientSideSessionFactory
import play.api.mvc._

final class DuplicateDisposalError @Inject()
                                   (implicit clientSideSessionFactory: ClientSideSessionFactory)
  extends Controller {

  def present = Action { implicit request =>
    Ok(views.html.disposal_of_vehicle.duplicate_disposal_error())
  }
}
