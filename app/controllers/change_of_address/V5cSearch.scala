package controllers.change_of_address

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import views._
import models.domain.change_of_address.{V5cSearchModel, V5cSearchResponse, V5cSearchConfirmationModel}
import controllers.Mappings._
import scala.concurrent.Future
import play.api.libs.json.Json
import play.api.Logger
import play.api.libs.ws.WS
import scala.concurrent.{ExecutionContext, Future, Await}
import ExecutionContext.Implicits.global
import play.api.cache.Cache
import play.api.Play.current
import controllers.Mappings
import modules.{injector}

object V5cSearch extends Controller {

  val v5cSearchForm = Form(
    mapping(
      app.ChangeOfAddress.V5cReferenceNumberNID -> V5cReferenceNumber(minLength = 11, maxLength = 11),
      app.ChangeOfAddress.vehicleVRNID -> vehicleVRN(minLength = 2, maxLength = 7)
    )(V5cSearchModel.apply)(V5cSearchModel.unapply)
  )

  def present = Action { implicit request =>
    Ok(html.change_of_address.v5c_search(v5cSearchForm, fetchData))
  }
/*
  def submit = Action { implicit request =>
    v5cSearchForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(html.change_of_address.v5c_search(formWithErrors, fetchData()))
        },
        ToAddress => Redirect(routes.ConfirmVehicleDetails.present())
    )
  }
  */


  def submit = Action.async {
    implicit request => {
      v5cSearchForm.bindFromRequest.fold(
        formWithErrors => Future {
          Logger.debug("Form validation failed posted data = ${formWithErrors.errors}")
          BadRequest(html.change_of_address.v5c_search(formWithErrors, fetchData())) },
        v5cForm => {

          Logger.debug("Form validation has passed")
          Logger.debug("==========================")
          Logger.debug("Calling V5C micro service...")
          val webService = injector.getInstance(classOf[services.WebService])
          val result = webService.invoke(v5cForm).map { resp => {
            Logger.debug(s"Web service call successful - response = ${resp}")
            play.api.cache.Cache.set(Mappings.V5CRegistrationNumber.key, v5cForm.vehicleVRN)
            play.api.cache.Cache.set(Mappings.V5CReferenceNumber.key, v5cForm.V5cReferenceNumber)
            val key = v5cForm.V5cReferenceNumber + "." + v5cForm.vehicleVRN
            Logger.debug(s"V5cSearch looking for value for key: $key")
            play.api.cache.Cache.set(key, resp.v5cSearchConfirmationModel)
            Redirect(routes.ConfirmVehicleDetails.present())
          }}
            .recoverWith{
              case e: Throwable => {
                Future { 
            	  Logger.debug("Web service call failed")            	
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
    "Roger Booth"
  }
}
