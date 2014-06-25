package utils.helpers

import helpers.UnitSpec
import play.api.data.Forms._
import play.api.data._
import utils.helpers.FormExtensions._

final class FormExtensionsSpec extends UnitSpec {
  "anyMandatoryFields" should {
    "return true when the required constraint is on a field" in {
      final case class ModelWithMandatory(id1: String, id2: String)
      val formWithMandatory = Form(
        mapping(
          "id1" -> text(),
          "id2" -> nonEmptyText()
        )(ModelWithMandatory.apply)(ModelWithMandatory.unapply)
      )

      formWithMandatory.anyMandatoryFields should equal(true)
    }

    "return false when the required constraint is not on any field" in {
      final case class ModelWithoutMandatory(id1: String, id2: String)
      val formWithoutMandatory = Form(
        mapping(
          "id1" -> text(),
          "id2" -> text()
        )(ModelWithoutMandatory.apply)(ModelWithoutMandatory.unapply)
      )

      formWithoutMandatory.anyMandatoryFields should equal(false)
    }
  }

  "replaceError" should {
    "return an unchanged form when matching key and message are not found in errors list" in {
      final case class Model(id1: String, id2: String)
      val errorForId1: FormError = FormError(key = "id2", message = "error.anything", args = Seq.empty)
      val newErrorForId2: FormError = FormError(key = "id2", message = "error.somethingElse", args = Seq.empty)
      val fMapping = mapping(
        "id1" -> text(),
        "id2" -> text()
      )(Model.apply)(Model.unapply)
      val form = new Form(
        mapping = fMapping,
        data = Map.empty,
        errors = Seq(errorForId1), // It has error for id1 but not id2 so there should be no change.
        value = None
      )

      form.replaceError("id2", "error.shouldNotExist", newErrorForId2) should equal(form)
    }

    "return form with replaced error message when matching key and message are found in errors list" in {
      final case class Model(id1: String, id2: String)
      val errorForId1: FormError = FormError(key = "id2", message = "error.anything", args = Seq.empty)
      val newErrorForId2: FormError = FormError(key = "id2", message = "error.somethingElse", args = Seq.empty)
      val fMapping = mapping(
        "id1" -> text(),
        "id2" -> text()
      )(Model.apply)(Model.unapply)
      val form = new Form(
        mapping = fMapping,
        data = Map.empty,
        errors = Seq(errorForId1), // It has error for id1 but not id2 so there should be no change.
        value = None
      )
      val expectedForm = new Form(
        mapping = fMapping,
        data = Map.empty,
        errors = Seq(newErrorForId2),
        value = None
      )

      form.replaceError("id2", "error.anything", newErrorForId2) should equal(expectedForm)
    }

    "return an unchanged form when matching key is not found in errors list" in {
      final case class Model(id1: String, id2: String)
      val errorForId1: FormError = FormError(key = "id1", message = "error.anything", args = Seq.empty)
      val newErrorForId2: FormError = FormError(key = "id2", message = "error.somethingElse", args = Seq.empty)
      val fMapping = mapping(
        "id1" -> text(),
        "id2" -> text()
      )(Model.apply)(Model.unapply)
      val form = new Form(
        mapping = fMapping,
        data = Map.empty,
        errors = Seq(errorForId1), // It has error for id1 but not id2 so there should be no change.
        value = None
      )

      form.replaceError("id2", newErrorForId2) should equal(form)
    }

    "return form with replaced error message when matching key is found in errors list" in {
      final case class Model(id1: String, id2: String)
      val errorForId2: FormError = FormError(key = "id2", message = "error.anything", args = Seq.empty)
      val newErrorForId2: FormError = FormError(key = "id2", message = "error.somethingElse", args = Seq.empty)
      val fMapping = mapping(
        "id1" -> text(),
        "id2" -> text()
      )(Model.apply)(Model.unapply)
      val form = new Form(
        mapping = fMapping,
        data = Map.empty,
        errors = Seq(errorForId2), // It has error for id2 so it should be detected and changed.
        value = None
      )
      val expectedForm = new Form(
        mapping = fMapping,
        data = Map.empty,
        errors = Seq(newErrorForId2),
        value = None
      )

      form.replaceError("id2", newErrorForId2) should equal(expectedForm)
    }
  }
}
