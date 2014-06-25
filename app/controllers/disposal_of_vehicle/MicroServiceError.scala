package controllers.disposal_of_vehicle

import com.google.inject.Inject
import common.ClientSideSessionFactory
import play.api.mvc._

final class MicroServiceError @Inject()(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {
  def present = Action { implicit request =>
    Ok(views.html.disposal_of_vehicle.micro_service_error())
  }
}
