package composition

import com.google.inject.Guice
import play.api.mvc.EssentialFilter
import filters.EnsureSessionCreatedFilter

//import play.filters.gzip.GzipFilter
import play.filters.csrf.CSRFFilter

object Composition {
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
  lazy val devInjector = Guice.createInjector(DevModule)

  lazy val filters = Array(new CSRFFilter(), devInjector.getInstance(classOf[EnsureSessionCreatedFilter]))
//  lazy val filters: EssentialFilter = new GzipFilter()

}
