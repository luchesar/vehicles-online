package controllers.change_of_address

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import views._
import models.domain.change_of_address.{LoginConfirmationModel, V5cSearchModel, V5cSearchResponse, V5cSearchConfirmationModel}
import controllers.Mappings._
import play.api.Logger
import scala.concurrent.{ExecutionContext, Future, Await}
import ExecutionContext.Implicits.global
import play.api.cache.Cache
import play.api.Play.current
import controllers.Mappings
import modules.{injector}

object V5cSearch extends Controller {

  val v5cSearchForm = Form(
    mapping(
      app.ChangeOfAddress.v5cReferenceNumberID -> V5cReferenceNumber(minLength = 11, maxLength = 11),
      app.ChangeOfAddress.v5cRegistrationNumberID -> V5CRegistrationNumber(minLength = 2, maxLength = 7)
    )(V5cSearchModel.apply)(V5cSearchModel.unapply)
  )

  def present = Action { implicit request =>
    Ok(html.change_of_address.v5c_search(v5cSearchForm, fetchData))
  }

  def submit = Action.async {
    implicit request => {
      v5cSearchForm.bindFromRequest.fold(
        formWithErrors => Future {
          Logger.debug(s"Form validation failed posted data = ${formWithErrors.errors}")
          BadRequest(html.change_of_address.v5c_search(formWithErrors, fetchData())) },
        v5cForm => {

          Logger.debug("V5cSearch form validation has passed")
          Logger.debug("Calling V5C micro service...")
          val webService = injector.getInstance(classOf[services.WebService])
          val result = webService.invoke(v5cForm).map { resp => {
            Logger.debug(s"Web service call successful - response = ${resp}")

            play.api.cache.Cache.set(Mappings.V5cRegistrationNumber.key, v5cForm.v5cRegistrationNumber)
            play.api.cache.Cache.set(Mappings.V5cReferenceNumber.key, v5cForm.v5cReferenceNumber)
            val key = v5cForm.v5cReferenceNumber + "." + v5cForm.v5cRegistrationNumber
            Logger.debug(s"V5cSearch storing data returned from micro service in cache using key: $key")
            play.api.cache.Cache.set(key, resp.v5cSearchConfirmationModel)

            Redirect(routes.ConfirmVehicleDetails.present())
          }}
            .recoverWith{
              case e: Throwable => {
                Future { 
            	  Logger.debug(s"Web service call failed. Stacktrace: ${e.getStackTrace}")
            	  BadRequest("The remote server didn't like the request.")
                }
              }
            }
            result
        }
      )
    }
  }

  def fetchData(): String = {
    val key = Mappings.LoginConfirmationModel.key
    val result = Cache.getAs[LoginConfirmationModel](key)

    result match {
      case Some(loginConfirmationModel) => loginConfirmationModel.firstName
      case _ => "Roger Booth"
    }
  }
}
