package services.csrf_prevention

import play.api.mvc._

class CSRFFilter() extends EssentialFilter {
  def apply(next: EssentialAction): EssentialAction = new CSRFAction(next)
}