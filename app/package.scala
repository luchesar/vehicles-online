import java.io.File
import java.util.UUID

import com.typesafe.config.ConfigFactory
import composition.Composition
import filters.WithFilters
import play.api._
import play.api.i18n.Lang
import play.api.mvc.Results._
import play.api.mvc.{SimpleResult, RequestHeader}
import utils.helpers.ErrorStrategy

import scala.concurrent.Future

package object app {

  import play.api.Play
  import scala.util.{Success, Try}

  object ConfigProperties {
    def getProperty(property: String, default: Int) = Try(Play.current.configuration.getInt(property).getOrElse(default)) match {
      case Success(s) => s
      case _ => default
    }

    def getProperty(property: String, default: String) = Try(Play.current.configuration.getString(property).getOrElse(default)) match {
      case Success(s) => s
      case _ => default
    }

    def getProperty(property: String, default: Boolean) = Try(Play.current.configuration.getBoolean(property).getOrElse(default)) match {
      case Success(s) => s
      case _ => default
    }

    def getProperty(property: String, default: Long) = Try(Play.current.configuration.getLong(property).getOrElse(default)) match {
      case Success(s) => s
      case _ => default
    }
  }


}
