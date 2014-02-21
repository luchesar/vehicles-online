import com.google.inject.Guice
import java.io.File
import com.typesafe.config.ConfigFactory
import java.util.UUID
import modules.{DevModule, TestModule}
import play.api._
import play.api.Configuration
import play.api.mvc._
import play.api.mvc.Results._
import scala.concurrent.{ExecutionContext, Future}
import play.api.Play.current
import ExecutionContext.Implicits.global
import utils.helpers.Config

/**
 * Application configuration is in a hierarchy of files:
 *
 *                            application.conf
 *                      /             |            \
 * application.prod.conf    application.dev.conf    application.test.conf <- these can override and add to application.conf
 *
 * play test  <- test mode picks up application.test.conf
 * play run   <- dev mode picks up application.dev.conf
 * play start <- prod mode picks up application.prod.conf
 *
 * To override and stipulate a particular "conf" e.g.
 * play -Dconfig.file=conf/application.test.conf run
 */

object Global extends GlobalSettings {
  // Play.isTest will evaluate to true when you run "play test" from the command line
  // If play is being run to execute the tests then use the TestModule to provide fake
  // implementations of traits otherwise use the DevModule to provide the real ones
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
  def module = if (Play.isTest) TestModule else DevModule

  lazy val injector = Guice.createInjector(module)


  /**
   * Controllers must be resolved through the application context. There is a special method of GlobalSettings
   * that we can override to resolve a given controller. This resolution is required by the Play router.
   */
  override def getControllerInstance[A](controllerClass: Class[A]): A = injector.getInstance(controllerClass)

  override def onStart(app: Application) {
    Logger.info("vehicles-online Started") // used for operations, do not remove
    val endPoint = s"${Config.ordnanceSurveyBaseUrl}"
    Logger.debug(s"Ordnance survey base url = ${endPoint}")
    val username = s"${Config.ordnanceSurveyUsername}"
    Logger.debug(s"Ordnance survey username = ${username}")
    val password = s"${Config.ordnanceSurveyPassword}"
    Logger.debug(s"Ordnance survey password = ${password}")
  }

  override def onLoadConfig(configuration: Configuration, path: File, classloader: ClassLoader, mode: Mode.Mode): Configuration = {
    val dynamicConfig = Configuration.from(Map("session.cookieName" -> UUID.randomUUID().toString.substring(0, 16)))
    val applicationConf = System.getProperty("config.file", s"application.${mode.toString.toLowerCase}.conf")
    val environmentOverridingConfiguration = configuration ++
      Configuration(ConfigFactory.load(applicationConf)) ++
      Configuration(ConfigFactory.load("vehiclesOnline.conf")) ++
      dynamicConfig
    super.onLoadConfig(environmentOverridingConfiguration, path, classloader, mode)
  }

  override def onStop(app: Application) {
    super.onStop(app)
    Logger.info("vehicles-online Stopped") // used for operations, do not remove
  }

  // 404 - page not found error http://alvinalexander.com/scala/handling-scala-play-framework-2-404-500-errors
  override def onHandlerNotFound(request: RequestHeader): Future[SimpleResult] = Future(NotFound(views.html.errors.onHandlerNotFound(request)))
}

