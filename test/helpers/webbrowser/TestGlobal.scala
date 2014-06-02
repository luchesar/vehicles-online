package helpers.webbrowser

import com.typesafe.config.ConfigFactory
import controllers.disposal_of_vehicle.routes
import java.io.File
import java.util.UUID
import javax.crypto.BadPaddingException
import play.api._
import play.api.Configuration
import play.api.mvc._
import play.api.mvc.Results._
import play.api.Play.current
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import utils.helpers.CryptoHelper
import com.google.inject.Injector
import composition.TestComposition._

/**
 * Application configuration is in a hierarchy of files:
 *
 * application.conf
 * /             |            \
 * application.prod.conf    application.dev.conf    application.test.conf <- these can override and add to application.conf
 *
 * play test  <- test mode picks up application.test.conf
 * play run   <- dev mode picks up application.dev.conf
 * play start <- prod mode picks up application.prod.conf
 *
 * To override and stipulate a particular "conf" e.g.
 * play -Dconfig.file=conf/application.test.conf run
 */

object TestGlobal extends WithFilters(filters: _*) with GlobalSettings {
  private lazy val injector: Injector = testInjector

  /**
   * Controllers must be resolved through the application context. There is a special method of GlobalSettings
   * that we can override to resolve a given controller. This resolution is required by the Play router.
   */
  override def getControllerInstance[A](controllerClass: Class[A]): A = injector.getInstance(controllerClass)

  override def onLoadConfig(configuration: Configuration, path: File, classloader: ClassLoader, mode: Mode.Mode): Configuration = {
    val dynamicConfig = Configuration.from(Map("session.cookieName" -> UUID.randomUUID().toString.substring(0, 16)))
    val applicationConf = System.getProperty("config.file", s"application.${mode.toString.toLowerCase}.conf")
    val environmentOverridingConfiguration = configuration ++
      Configuration(ConfigFactory.load(applicationConf)) ++
      dynamicConfig
    super.onLoadConfig(environmentOverridingConfiguration, path, classloader, mode)
  }

  // 404 - page not found error http://alvinalexander.com/scala/handling-scala-play-framework-2-404-500-errors
  override def onHandlerNotFound(request: RequestHeader): Future[SimpleResult] = Future(NotFound(views.html.errors.onHandlerNotFound(request)))

  override def onError(request: RequestHeader, ex: Throwable): Future[SimpleResult] = ex.getCause match {
    case _: BadPaddingException  => CryptoHelper.handleApplicationSecretChange(request)
    case _ => Future(Redirect(routes.Error.present()))
  }
}

