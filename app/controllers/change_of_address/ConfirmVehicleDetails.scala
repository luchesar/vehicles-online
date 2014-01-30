package controllers.change_of_address

import play.api.mvc._
import play.api.cache.Cache
import play.api.Play.current
import play.api.data._
import play.api.data.Forms._
import play.api.Logger

import views._
import models.domain.change_of_address.V5cSearchConfirmationModel
import controllers.Mappings


object ConfirmVehicleDetails extends Controller {

  def present = Action { implicit request =>
    fetchData() match {
      case Some(v5csearchConfirmationModel) => Ok(html.change_of_address.confirm_vehicle_details(v5csearchConfirmationModel))
      case None => Redirect(routes.V5cSearch.present())
    }
  }

  def submit = Action {
    Redirect(routes.ConfirmVehicleDetails.present)
  }

  def fetchData(): Option[V5cSearchConfirmationModel] = {
    val v5cRegistrationNumberOption = play.api.cache.Cache.getAs[String](Mappings.V5cRegistrationNumber.key)
    v5cRegistrationNumberOption match {
      case Some(v5cRegistrationNumber) => {
        val v5cReferenceNumberOption = play.api.cache.Cache.getAs[String](Mappings.V5cReferenceNumber.key)
        v5cReferenceNumberOption match {
          case Some(v5cReferenceNumber) => {
            val key = v5cReferenceNumber + "." + v5cRegistrationNumber
            Logger.debug(s"ConfirmVehicleDetails looking for Cache key = ${key}")
            play.api.cache.Cache.getAs[V5cSearchConfirmationModel](key)
          }
          case None => {
            Logger.warn(s"ConfirmVehicleDetails could not find value in Cache for key ${Mappings.V5cReferenceNumber.key}")
            None
          }

        }
      }
      case None => {
        Logger.warn(s"ConfirmVehicleDetails could not find value in Cache for key ${Mappings.V5cRegistrationNumber.key}")
        None
      }
    }
  }

}
