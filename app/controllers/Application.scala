package controllers

import app.ConfigProperties._
import play.api.mvc._

/* Controller for redirecting people to the start page if the enter the application using the url "/"
* This allows us to change the start page using the config file without having to change any code. */
object Application extends Controller {
  val startUrl: String = getProperty("start.page", "/disposal-of-vehicle/before-you-start")

  def index = Action {
    Redirect(startUrl)
  }
}