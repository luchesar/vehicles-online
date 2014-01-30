package controllers.change_of_address

import play.api.data.Forms._
import play.api.mvc._
import play.api.data.Form
import play.api.Logger
import modules._
import models.domain.change_of_address.LoginPageModel
import play.api.cache.Cache
import controllers.Mappings
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import play.api.Play.current

object LoginPage extends Controller {

  val loginPageForm = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )(LoginPageModel.apply)(LoginPageModel.unapply)
  )

  // Note that the request must be made implicit so play can extract the language header for I18N
  def present = Action {
    implicit request =>
      Logger.info("LoginPage Preferred languages (in order) from browser: " + request.acceptLanguages.map(_.code).mkString(", "))
      Ok(views.html.change_of_address.login_page(loginPageForm))
  }

  // TODO [SKW] break the method below into several smaller methods to make it more readable.
  def submit = Action.async {
    implicit request => {
      loginPageForm.bindFromRequest.fold(
        formWithErrors => Future {
          BadRequest(views.html.change_of_address.login_page(formWithErrors))
        },
        loginPageForm => {

          Logger.debug("LoginPage form validation has passed")
          Logger.debug("LoginPage calling login micro service...")

          val webService = injector.getInstance(classOf[services.LoginWebService])
          val result = webService.invoke(loginPageForm).map {
            resp => {
              Logger.debug(s"LoginPage Web service call successful - response = ${resp}")

              val key = Mappings.LoginConfirmationModel.key
              Cache.set(key, resp.loginConfirmationModel)
              Logger.debug(s"LoginPage set value for key: $key")
              Redirect(routes.LoginConfirmation.present())
            }
          }
            .recoverWith {
            case e: Throwable => {
              Future {
                Logger.error("Web service call failed")
                BadRequest("The remote server didn't like the request.")
              }
            }
          }
          result
        }
      )
    }
  }

}