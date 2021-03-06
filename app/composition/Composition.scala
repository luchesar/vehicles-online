package composition

import com.google.inject.Guice
import filters.{AccessLoggingFilter, EnsureSessionCreatedFilter}
import play.filters.gzip.GzipFilter
import filters.csrf_prevention.CsrfPreventionFilter
import utils.helpers.ErrorStrategy

trait Composition {
  lazy val injector = Guice.createInjector(DevModule)

  lazy val filters = Array(
    injector.getInstance(classOf[EnsureSessionCreatedFilter]),
    new GzipFilter(),
    injector.getInstance(classOf[AccessLoggingFilter]),
    injector.getInstance(classOf[CsrfPreventionFilter])
  )

  lazy val errorStrategy = injector.getInstance(classOf[ErrorStrategy])
}