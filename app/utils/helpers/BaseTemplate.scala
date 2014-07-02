package utils.helpers

import views.html.helper.FieldConstructor

object BaseTemplate {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.baseTemplate.f)
}