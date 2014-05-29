package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.Logger
import common.CookieImplicits.{RequestCookiesAdapter, SimpleResultAdapter}
import mappings.disposal_of_vehicle.RelatedCacheKeys
import models.domain.disposal_of_vehicle.TraderDetailsModel
import common.ClientSideSessionFactory
import com.google.inject.Inject

final class VrmLocked @Inject()()(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {

  def present = Action { implicit request =>
    Logger.debug(s"VrmLocked - displaying the vrm locked error page")
    Ok(views.html.disposal_of_vehicle.vrm_locked())
  }

  def submit = Action { implicit request =>
    val formData = request.body.asFormUrlEncoded.getOrElse(Map.empty[String, Seq[String]])
    val actionValue = formData.get("action").flatMap(_.headOption)
    actionValue match {
      case Some("newDisposal") =>
        newDisposal
      case Some("exit") =>
        exit
      case _ => BadRequest("This action is not allowed") // TODO redirect to error page ?
    }
  }

  private def newDisposal(implicit request: Request[AnyContent]): SimpleResult = {
    request.cookies.getModel[TraderDetailsModel] match {
      case (Some(traderDetails)) =>
        Redirect(routes.VehicleLookup.present()).discardingCookies(RelatedCacheKeys.DisposeSet)
      case _ =>
        Redirect(routes.SetUpTradeDetails.present())
    }
  }

  private def exit(implicit request: Request[AnyContent]): SimpleResult = {
    Redirect(routes.BeforeYouStart.present()).discardingCookies(RelatedCacheKeys.FullSet)
  }
}

