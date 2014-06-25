package filters

import play.api.GlobalSettings
import play.api.mvc.{Filters, EssentialAction, EssentialFilter}

class WithFilters(filters: => Array[EssentialFilter]) extends GlobalSettings {
  override def doFilter(a: EssentialAction): EssentialAction = {
    Filters(super.doFilter(a), filters: _*)
  }
}
