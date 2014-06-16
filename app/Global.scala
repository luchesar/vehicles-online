import java.io.File
import java.util.UUID

import com.google.inject.Injector
import com.typesafe.config.ConfigFactory
import filters.EnsureSessionCreatedFilter
import play.api._
import play.api.mvc.Results._
import play.api.mvc._

//import play.filters.gzip._
import composition.Composition._
import utils.helpers.ErrorStrategy

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

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

object Global extends WithFilters(filters: _*) with GlobalSettings {

  private lazy val injector: Injector = devInjector

  /**
   * Controllers must be resolved through the application context. There is a special method of GlobalSettings
   * that we can override to resolve a given controller. This resolution is required by the Play router.
   */
  override def getControllerInstance[A](controllerClass: Class[A]): A = injector.getInstance(controllerClass)

  override def onStart(app: Application) {
    Logger.info("vehicles-online Started") // used for operations, do not remove
  }

  override def onLoadConfig(configuration: Configuration, path: File, classloader: ClassLoader, mode: Mode.Mode): Configuration = {
    val dynamicConfig = Configuration.from(Map("session.cookieName" -> UUID.randomUUID().toString.substring(0, 16)))
    val applicationConf = System.getProperty("config.file", s"application.${mode.toString.toLowerCase}.conf")
    val environmentOverridingConfiguration = configuration ++
      Configuration(ConfigFactory.load(applicationConf)) ++
      dynamicConfig
    super.onLoadConfig(environmentOverridingConfiguration, path, classloader, mode)
  }

  override def onStop(app: Application) {
    super.onStop(app)
    Logger.info("vehicles-online Stopped") // used for operations, do not remove
  }

  // 404 - page not found error http://alvinalexander.com/scala/handling-scala-play-framework-2-404-500-errors
  override def onHandlerNotFound(request: RequestHeader): Future[SimpleResult] = {
    Logger.warn(s"Broken link returning http code 404. uri: ${request.uri}")
    Future(NotFound(views.html.errors.onHandlerNotFound(request)))
  }

  override def onError(request: RequestHeader, ex: Throwable): Future[SimpleResult] =
    ErrorStrategy(request, ex)

  override def doFilter(a: EssentialAction): EssentialAction = {
    Filters(super.doFilter(a), devInjector.getInstance(classOf[EnsureSessionCreatedFilter]))
  }
}
