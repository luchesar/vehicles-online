package composition

import com.google.inject.Guice
import filters.{AccessLoggingFilter, EnsureSessionCreatedFilter}
import services.csrf_prevention.CSRFPreventionFilter
import play.filters.gzip.GzipFilter
import utils.helpers.ErrorStrategy


trait Composition {
  lazy val injector = Guice.createInjector(DevModule)

  lazy val filters = Array(
    new GzipFilter(),
    injector.getInstance(classOf[EnsureSessionCreatedFilter]),
    injector.getInstance(classOf[AccessLoggingFilter]),
    injector.getInstance(classOf[CSRFPreventionFilter])
  )

  lazy val errorStrategy = injector.getInstance(classOf[ErrorStrategy])
}
