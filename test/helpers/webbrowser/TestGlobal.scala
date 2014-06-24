package helpers.webbrowser

import java.io.File
import java.util.UUID

import com.google.inject.Injector
import com.typesafe.config.ConfigFactory
import composition.TestComposition.{filters => appFilters, _}
import play.api._
import play.api.mvc.Results._
import play.api.mvc._
import services.csrf_prevention.CSRFPreventionFilter
import utils.helpers.ErrorStrategy

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import filters._

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

object TestGlobal extends GlobalSettings {
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

  override def onError(request: RequestHeader, ex: Throwable): Future[SimpleResult] =
    ErrorStrategy(request, ex)

  override def doFilter(a: EssentialAction): EssentialAction = {
    // TODO can we work out how to dependency inject into WithFilters?
    // EnsureSessionCreatedFilter relies on IoC which in turn requires the configs to be read, but if we try to load it in the
    // Global WithFilters the config is not yet loaded so will fail. One solution is to use this doFilter to append to the
    // Global filters.
    Filters(
      super.doFilter(a),
      testInjector.getInstance(classOf[EnsureSessionCreatedFilter]),
      testInjector.getInstance(classOf[AccessLoggingFilter]),
      new CSRFPreventionFilter()
    )
  }
}

