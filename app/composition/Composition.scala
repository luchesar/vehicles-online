package composition

import com.google.inject.Guice
import services.csrf_prevention.CSRFPreventionFilter
import play.filters.gzip.GzipFilter


object Composition {
  lazy val devInjector = Guice.createInjector(DevModule)
  lazy val filters = Array(new GzipFilter())
}
