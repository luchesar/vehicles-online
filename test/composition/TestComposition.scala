package composition

import com.google.inject.{Injector, Guice}
import filters.EnsureSessionCreatedFilter
import services.csrf_prevention.CSRFFilter

object TestComposition {
  lazy val testInjector: Injector = Guice.createInjector(new TestModule())

  lazy val filters = Array(new CSRFFilter(), testInjector.getInstance(classOf[EnsureSessionCreatedFilter]))

}