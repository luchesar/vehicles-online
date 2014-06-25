package composition

import com.google.inject.Guice
import filters.{AccessLoggingFilter, EnsureSessionCreatedFilter}
import services.csrf_prevention.CSRFPreventionFilter
import play.filters.gzip.GzipFilter


object Composition {
  lazy val devInjector = Guice.createInjector(DevModule)
  lazy val filters = Array(
    new GzipFilter(),
    devInjector.getInstance(classOf[EnsureSessionCreatedFilter]),
    devInjector.getInstance(classOf[AccessLoggingFilter]),
    devInjector.getInstance(classOf[CSRFPreventionFilter])
  )
}
