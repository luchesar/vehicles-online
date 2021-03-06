package controllers.disposal_of_vehicle

import common.ClientSideSessionFactory
import javax.inject.Inject
import play.api.mvc._
import utils.helpers.Config

final class DuplicateDisposalError @Inject()()
                                   (implicit clientSideSessionFactory: ClientSideSessionFactory, config: Config)
  extends Controller {

  def present = Action { implicit request =>
    Ok(views.html.disposal_of_vehicle.duplicate_disposal_error())
  }
}