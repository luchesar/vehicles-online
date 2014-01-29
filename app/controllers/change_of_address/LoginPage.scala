package controllers.change_of_address

import play.api.data.Forms._
import play.api.mvc._
import views._
import play.api.data.Form
import models.domain.change_of_address.LoginPageModel
import play.api.i18n.Messages
import play.api.i18n.Lang
import scala.concurrent.Future
import play.api.Logger
import modules._
import models.domain.change_of_address.LoginPageModel
import play.api.cache.Cache
import controllers.Mappings
import scala.concurrent.{ExecutionContext, Future, Await}
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
  def present = Action { implicit request =>
    Logger.info("LoginPage Preferred languages (in order) from browser: " + request.acceptLanguages.map(_.code).mkString(", "))
    Ok(html.change_of_address.login_page(loginPageForm))
  }
/*
  def submit = Action {
    Redirect(routes.LoginConfirmation.present)
  }
  */

  def submit = Action.async {
    implicit request => {
      loginPageForm.bindFromRequest.fold(
        formWithErrors => Future { BadRequest(html.change_of_address.login_page(formWithErrors)) },
        loginPageForm => {

          Logger.debug("LoginPage form validation has passed")
          Logger.debug("LoginPage calling login micro service...")

          val webService = injector.getInstance(classOf[services.LoginWebService])
          val result = webService.invoke(loginPageForm).map { resp => {
            Logger.debug(s"LoginPage Web service call successful - response = ${resp}")

            val key = Mappings.LoginConfirmationModel.key
            Logger.debug(s"LoginPage looking for value for key: $key")
            Cache.set(key, resp.loginConfirmationModel)
            Redirect(routes.LoginConfirmation.present())
          }}
            .recoverWith{
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