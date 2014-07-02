package composition

import com.google.inject.Guice
import filters.{AccessLoggingFilter, EnsureSessionCreatedFilter}
import services.csrf_prevention.CsrfPreventionFilter
import play.filters.gzip.GzipFilter
import utils.helpers.ErrorStrategy

trait Composition {
  lazy val injector = Guice.createInjector(DevModule)

  lazy val filters = Array(
    new GzipFilter(),
    injector.getInstance(classOf[EnsureSessionCreatedFilter]),
    injector.getInstance(classOf[AccessLoggingFilter]),
    injector.getInstance(classOf[CsrfPreventionFilter])
  )

  lazy val errorStrategy = injector.getInstance(classOf[ErrorStrategy])
}