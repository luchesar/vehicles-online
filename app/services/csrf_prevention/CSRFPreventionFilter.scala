package services.csrf_prevention

import play.api.mvc._

class CSRFPreventionFilter() extends EssentialFilter {
  def apply(next: EssentialAction): EssentialAction = {
    new CSRFPreventionAction(next)
  }
}