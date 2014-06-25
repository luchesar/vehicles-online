package composition

import com.google.inject.{Injector, Guice}
import filters.{AccessLoggingFilter, EnsureSessionCreatedFilter}
import play.filters.gzip.GzipFilter
import services.csrf_prevention.CSRFPreventionFilter

object TestComposition {
  lazy val testInjector: Injector = Guice.createInjector(new TestModule())

  lazy val filters = Array(
    new GzipFilter(),
    testInjector.getInstance(classOf[EnsureSessionCreatedFilter]),
    testInjector.getInstance(classOf[AccessLoggingFilter]),
    testInjector.getInstance(classOf[CSRFPreventionFilter])
  )

}