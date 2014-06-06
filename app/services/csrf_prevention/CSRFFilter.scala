package services.csrf_prevention

import play.api.mvc._
import services.csrf_prevention.CSRF.{TokenProvider, ErrorHandler}
import play.api.Play

class CSRFFilter() extends EssentialFilter {
  def apply(next: EssentialAction): EssentialAction = new CSRFAction(next)
}

object CSRFFilter {
  def apply() = new CSRFFilter()
}