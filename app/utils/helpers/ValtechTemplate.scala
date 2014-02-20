package utils.helpers

import views.html.helper.FieldConstructor

object ValtechTemplate {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.valtechTemplate.f)
}

/* TODO: Speak to Scott and Ian and check if the below are needed as uncommented the app doesnt run
object EmptyTemplate {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.emptyTemplate.f)
}


object ShortFieldTemplate {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.shortFieldTemplate.f)
}

object CurrencyTemplate {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.shortFieldCurrencyTemplate.f)
}

object DatepickerTemplate {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.datepickerTemplate.f)
}

object DeclareCheckTemplate {
  implicit val fieldConstructor = FieldConstructor(views.html.helper.templates.declareCheckTemplate.f)
}*/