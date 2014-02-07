package controllers

import app.ConfigProperties._
import play.api.mvc._

object Application extends Controller {
  val startUrl: String = getProperty("start.page", "/disposal-of-vehicle/before-you-start")

  def index = Action {
    Redirect(startUrl)
  }
}