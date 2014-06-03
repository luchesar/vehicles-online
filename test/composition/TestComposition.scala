package composition

import com.google.inject.{Injector, Guice}
import play.api.mvc.EssentialFilter
import play.filters.csrf.CSRFFilter

object TestComposition {
  lazy val testInjector: Injector = Guice.createInjector(new TestModule())

  lazy val filters: EssentialFilter = new CSRFFilter()

}