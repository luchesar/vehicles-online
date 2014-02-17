package controllers.change_of_address

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.domain.change_of_address.{LoginConfirmationModel, V5cSearchModel}
import play.api.Logger
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import play.api.cache.Cache
import play.api.Play.current
import controllers.change_of_address.Helpers._
import mappings.V5cSearch._
import mappings.{V5cRegistrationNumber, V5cReferenceNumber}
import mappings.V5cReferenceNumber._
import mappings.V5cRegistrationNumber._
import mappings.common.PostCode
import PostCode._
import javax.inject.Inject

class VehicleSearch @Inject() (webService: services.V5cSearchWebService) extends Controller {
  val vehicleSearchForm = Form(
    mapping(
      v5cReferenceNumberId -> v5cReferenceNumber(minLength = 11, maxLength = 11),
      v5cRegistrationNumberId -> v5CRegistrationNumber(minLength = 2, maxLength = 7),
      v5cPostcodeId -> postcode()
    )(V5cSearchModel.apply)(V5cSearchModel.unapply)
  )

  def present = Action { implicit request =>
    isUserLoggedIn() match {
      case true => Ok(views.html.change_of_address.v5c_search(vehicleSearchForm, fetchData))
      case false => Redirect(routes.AreYouRegistered.present)
    }
  }

  def submit = Action.async { implicit request => {
      vehicleSearchForm.bindFromRequest.fold(
        formWithErrors => Future {
          Logger.debug(s"Form validation failed posted data = ${formWithErrors.errors}")
          BadRequest(views.html.change_of_address.v5c_search(formWithErrors, fetchData()))
        },
        v5cForm => {
          Logger.debug("V5cSearch form validation has passed")
          Logger.debug("Calling V5C micro service...")
          val result = webService.invoke(v5cForm).map { resp =>
            Logger.debug(s"Web service call successful - response = ${resp}")

            Cache.set(V5cRegistrationNumber.key, v5cForm.v5cRegistrationNumber)
            Cache.set(V5cReferenceNumber.key, v5cForm.v5cReferenceNumber)

            val key = v5cForm.v5cReferenceNumber + "." + v5cForm.v5cRegistrationNumber
            Logger.debug(s"V5cSearch storing data returned from micro service in cache using key: $key")
            Cache.set(key, resp.v5cSearchConfirmationModel)

            Redirect(routes.ConfirmVehicleDetails.present)
          }.recoverWith {
            case e: Throwable => Future {
              Logger.debug(s"Web service call failed. Stacktrace: ${e.getStackTrace}")
              BadRequest("The remote server didn't like the request.")
            }
          }
          result
        }
      )
    }
  }

  private def fetchData(): String = {
    val key = mappings.LoginConfirmation.key
    val result = Cache.getAs[LoginConfirmationModel](key)

    result match {
      case Some(loginConfirmationModel) => loginConfirmationModel.firstName
      case _ => "Roger Booth"
    }
  }
}
