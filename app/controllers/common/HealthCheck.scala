package controllers.common

import play.api.mvc.{Action, Controller}

class HealthCheck extends Controller {

  def respond = Action { request =>
    Ok("")
  }

}