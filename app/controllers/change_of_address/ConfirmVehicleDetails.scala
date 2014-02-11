package controllers.change_of_address

import play.api.mvc._
import play.api.Play.current
import controllers.change_of_address.Helpers._
import models.domain.change_of_address.V5cSearchConfirmationModel
import play.api.cache.Cache
import mappings.{V5cRegistrationNumber, V5cReferenceNumber}

object ConfirmVehicleDetails extends Controller {

  def present = Action {
    implicit request =>

      isUserLoggedIn() match {
        case true => {
          fetchSearchConfirmationModelFromCache match {
            case Some(v5csearchConfirmationModel) => Ok(views.html.change_of_address.confirm_vehicle_details(v5csearchConfirmationModel))
            case None => Redirect(routes.VehicleSearch.present)
          }
        }
        case false => Redirect(routes.AreYouRegistered.present)
      }
  }

  def submit = Action {
    Redirect(routes.ConfirmVehicleDetails.present)
  }

  private def fetchSearchConfirmationModelFromCache: Option[V5cSearchConfirmationModel] = {
    Cache.getAs[String](V5cRegistrationNumber.key) match {
      case Some(regNum) => {
        val v5cReferenceNumberOption = Cache.getAs[String](V5cReferenceNumber.key)
        v5cReferenceNumberOption match {
          case Some(refNum) => {
            Cache.getAs[V5cSearchConfirmationModel](makeCacheKey(regNum, refNum))
          }
          case None => None
        }
      }
      case None => None
    }
  }

  private def makeCacheKey(regNum: String, refNum: String) = s"$refNum.$regNum"

  /*
    private def fetchSearchConfirmationModelFromCache2: Option[V5cSearchConfirmationModel] = {
      val v5cReferenceNumberOption = Cache.getAs[String](Mappings.V5cReferenceNumber.key)
      val v5cRegistrationNumberOption = Cache.getAs[String](Mappings.V5cRegistrationNumber.key)
      (v5cReferenceNumberOption, v5cRegistrationNumberOption) match {
        case (Some(refNum), Some(regNum)) =>
          Cache.getAs[V5cSearchConfirmationModel](makeCacheKey(regNum, refNum))
        case _ => None
      }
    }

    private def fetchSearchConfirmationModelFromCache3: Option[V5cSearchConfirmationModel] = {
      Cache.getAs[String](Mappings.V5cReferenceNumber.key).flatMap { refNum =>
        Cache.getAs[String](Mappings.V5cRegistrationNumber.key).flatMap { regNum =>
          Cache.getAs[V5cSearchConfirmationModel](makeCacheKey(regNum, refNum))
        }
      }
    }

    private def fetchSearchConfirmationModelFromCache4: Option[V5cSearchConfirmationModel] = {
      for { refNum <- Cache.getAs[String](Mappings.V5cReferenceNumber.key)
            regNum <- Cache.getAs[String](Mappings.V5cRegistrationNumber.key)
            model <- Cache.getAs[V5cSearchConfirmationModel](makeCacheKey(regNum, refNum))
      } yield model
    }


    */

}
