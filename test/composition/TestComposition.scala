package composition

import com.google.inject.{Injector, Guice}
import filters.{AccessLoggingFilter, EnsureSessionCreatedFilter}
import play.filters.gzip.GzipFilter
import services.csrf_prevention.CSRFPreventionFilter

trait TestComposition extends Composition {
  override lazy val injector: Injector = Guice.createInjector(new TestModule())
}