package controllers.disposal_of_vehicle

import com.google.inject.Inject
import common.ClientSideSessionFactory
import play.api.mvc._
import utils.helpers.Config

final class UprnNotFound @Inject()(config: Config)(implicit clientSideSessionFactory: ClientSideSessionFactory)  extends Controller {
  def present = Action { implicit request =>
    Ok(views.html.disposal_of_vehicle.uprn_not_found(config.prototypeBannerVisible))
  }
}
