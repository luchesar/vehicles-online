package controllers.disposal_of_vehicle

import com.google.inject.Inject
import common.ClientSideSessionFactory
import common.CookieImplicits.{RequestCookiesAdapter, SimpleResultAdapter}
import mappings.disposal_of_vehicle.RelatedCacheKeys
import mappings.disposal_of_vehicle.VrmLocked._
import models.domain.disposal_of_vehicle.BruteForcePreventionViewModel._
import models.domain.disposal_of_vehicle.{BruteForcePreventionViewModel, TraderDetailsModel}
import play.api.Logger
import play.api.mvc._

final class VrmLocked @Inject()()(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {
  def present = Action {
    implicit request =>
      request.cookies.getModel[BruteForcePreventionViewModel] match {
        case Some(viewModel) =>
          Logger.debug(s"VrmLocked - displaying the vrm locked error page")
          Ok(views.html.disposal_of_vehicle.vrm_locked(viewModel.dateTimeISOChronology))
        case None =>
          Logger.debug("VrmLocked - can't find cookie for BruteForcePreventionViewModel")
          Redirect(routes.VehicleLookup.present())
      }
  }

  def submit = Action { implicit request =>
    val formData = request.body.asFormUrlEncoded.getOrElse(Map.empty[String, Seq[String]])
    val actionValue = formData.get("action").flatMap(_.headOption)
    actionValue match {
      case Some(NewDisposalAction) => newDisposal
      case Some(ExitAction) => exit
      case _ => BadRequest("This action is not allowed") // TODO redirect to error page ?
    }
  }

  private def newDisposal(implicit request: Request[AnyContent]): SimpleResult =
    request.cookies.getModel[TraderDetailsModel] match {
      case (Some(traderDetails)) =>Redirect(routes.VehicleLookup.present()).discardingCookies(RelatedCacheKeys.DisposeSet)
      case _ =>Redirect(routes.SetUpTradeDetails.present())
    }

  private def exit(implicit request: Request[AnyContent]): SimpleResult =
    Redirect(routes.BeforeYouStart.present()).discardingCookies(RelatedCacheKeys.FullSet)
}

