package controllers.change_of_address

import models.domain.change_of_address.LoginConfirmationModel
import controllers.Mappings
import play.api.cache.Cache
import play.api.Play.current
import play.api.Logger

object Helpers {
  def isUserLoggedIn(): Boolean = {
    userLoginCredentials() match {
      case Some(loginConfirmationModel) => {
        Logger.debug("********* user logged in ************")
        true
      }
      case None => {
        Logger.debug("********* user not logged in *********")
        false
      }
    }
  }

  def userLoginCredentials(): Option[LoginConfirmationModel] = {
    val key = Mappings.LoginConfirmationModel.key
    val result = Cache.getAs[LoginConfirmationModel](key)
    result
  }
}
