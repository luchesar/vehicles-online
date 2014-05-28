package composition

import com.google.inject.{Injector, Guice}
import play.filters.gzip.GzipFilter
import play.api.mvc.EssentialFilter

object TestComposition {
  lazy val testInjector: Injector = Guice.createInjector(TestModule)

  lazy val filters: EssentialFilter = new GzipFilter()
}