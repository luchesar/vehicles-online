package utils.helpers

import views.html.helper.FieldConstructor

object EmptyTemplate {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.emptyTemplate.f)
}
