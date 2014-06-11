package controllers.disposal_of_vehicle

import com.google.inject.Inject
import common.ClientSideSessionFactory
import common.CookieImplicits.{RequestCookiesAdapter, SimpleResultAdapter}
import mappings.disposal_of_vehicle.RelatedCacheKeys
import models.domain.disposal_of_vehicle.BruteForcePreventionViewModel._
import models.domain.disposal_of_vehicle.{BruteForcePreventionViewModel, TraderDetailsModel}
import play.api.Logger
import play.api.mvc._
import mappings.common.Languages._
import scala.Some
import play.api.Play.current

final class VrmLocked @Inject()()(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {
  def present = Action {
    implicit request =>
      request.cookies.getModel[BruteForcePreventionViewModel] match {
        case Some(viewModel) =>
          Logger.debug(s"VLocked - Displaying the vrm locked error page")
          Ok(views.html.disposal_of_vehicle.vrm_locked(viewModel.dateTimeISOChronology))
        case None =>
          Logger.debug("VrmLocked - Can't find cookie for BruteForcePreventionViewModel")
          Redirect(routes.VehicleLookup.present())
      }
  }

  def newDisposal = Action { implicit request =>
    request.cookies.getModel[TraderDetailsModel] match {
      case (Some(traderDetails)) => Redirect(routes.VehicleLookup.present()).discardingCookies(RelatedCacheKeys.DisposeSet)
      case _ => Redirect(routes.SetUpTradeDetails.present())
    }
  }

  def exit = Action { implicit request =>
    Redirect(routes.BeforeYouStart.present()).discardingCookies(RelatedCacheKeys.FullSet)
  }

  def withLanguageCy = Action { implicit request =>
    Redirect(routes.VrmLocked.present()).
      withLang(langCy)
  }

  def withLanguageEn = Action { implicit request =>
    Redirect(routes.VrmLocked.present()).
      withLang(langEn)
  }

}

