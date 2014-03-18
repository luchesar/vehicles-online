package controllers.change_of_address

import play.api.data.Forms._
import play.api.mvc._
import play.api.data.Form
import play.api.Logger
import models.domain.change_of_address.LoginPageModel
import play.api.cache.Cache
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import play.api.Play.current
import javax.inject.Inject
import mappings.change_of_address

class LoginPage @Inject()(webService: services.LoginWebService) extends Controller {
  val loginPageForm = Form(
    mapping(
      mappings.change_of_address.LoginPage.usernameId -> nonEmptyText,
      mappings.change_of_address.LoginPage.passwordId -> nonEmptyText
    )(LoginPageModel.apply)(LoginPageModel.unapply)
  )

  // Note that the request must be made implicit so play can extract the language header for I18N
  def present = Action { implicit request =>
    Logger.info("LoginPage Preferred languages (in order) from browser: " + request.acceptLanguages.map(_.code).mkString(", "))
    Ok(views.html.change_of_address.login_page(loginPageForm))
  }

  def submit = Action.async { implicit request =>
    loginPageForm.bindFromRequest.fold(
      formWithErrors =>
        Future {
          BadRequest(views.html.change_of_address.login_page(formWithErrors))
        },
      loginPageModel => {
        Logger.debug("LoginPage form validation has passed")
        confirmLogin(webService, loginPageModel)
      }
    )
  }

  private def confirmLogin(webService: services.LoginWebService, loginPageForm: LoginPageModel): Future[SimpleResult] = {
    webService.invoke(loginPageForm).map { resp =>
      Logger.debug(s"LoginPage Web service call successful - response = ${resp}")
      val key = change_of_address.LoginConfirmation.key
      Cache.set(key, resp.loginConfirmationModel)
      Logger.debug(s"LoginPage set value for key: $key")
      Redirect(routes.LoginConfirmation.present)
    }.recoverWith {
      case e: Throwable => Future {
        Logger.debug(s"Web service call failed. Exception: ${e}")
        BadRequest("The remote server didn't like the request.")
      }
    }
  }

}