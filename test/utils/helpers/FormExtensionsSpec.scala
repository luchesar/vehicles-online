package utils.helpers

import helpers.UnitSpec
import play.api.data.Forms._
import play.api.data._
import utils.helpers.FormExtensions._

final class FormExtensionsSpec extends UnitSpec {
  "anyMandatoryFields" should {
    "return true when the required constraint is on a field" in {
      val formWithMandatory = Form(
        mapping(
          "id1" -> text(),
          "id2" -> nonEmptyText()
        )(Model.apply)(Model.unapply)
      )

      formWithMandatory.anyMandatoryFields should equal(true)
    }

    "return false when the required constraint is not on any field" in {
      val formWithoutMandatory = createForm()
      formWithoutMandatory.anyMandatoryFields should equal(false)
    }
  }

  "replaceError" should {
    "return an unchanged form when matching key and message are not found in errors list" in {
      val form = createForm(errors = Seq(errorForId1)) // It has error for id1 but not id2 so there should be no change.
      form.replaceError("id1", "error.shouldNotExist", newErrorForId2) should equal(form)
    }

    "return form with replaced error message when matching key and message are found in errors list" in {
      val form = createForm(errors = Seq(errorForId1)) // It has error for id1 but not id2 so there should be no change.
      val expectedForm = createForm(errors = Seq(newErrorForId2))
      form.replaceError("id1", "error.anything", newErrorForId2) should equal(expectedForm)
    }

    "return an unchanged form when matching key is not found in errors list" in {
      val form = createForm(errors = Seq(errorForId1)) // It has error for id1 but not id2 so there should be no change.
      form.replaceError("id2", newErrorForId2) should equal(form)
    }

    "return form with replaced error message when matching key is found in errors list" in {
      val form = createForm(errors = Seq(errorForId1)) // It has error for id2 so it should be detected and changed.
      val expectedForm = createForm(errors = Seq(newErrorForId2))
      form.replaceError("id1", newErrorForId2) should equal(expectedForm)
    }
  }

  "distinctErrors" should {
    "return an unchanged form when no duplicates present" in {
      val form = createForm(errors = Seq(errorForId1))
      form.distinctErrors should equal(form)
    }

    "return form with duplicates removed when duplicates are present" in {
      val form = createForm(errors = Seq(errorForId1, errorForId1, errorForId1))
      val expectedForm = createForm(errors = Seq(errorForId1))
      form.distinctErrors should equal(expectedForm)
    }
  }

  private final case class Model(id1: String, id2: String)

  private val formMappingWithoutMandatory: Mapping[Model] = mapping(
    "id1" -> text(),
    "id2" -> text()
  )(Model.apply)(Model.unapply)

  private val errorForId1: FormError = FormError(key = "id1", message = "error.anything", args = Seq.empty)
  private val newErrorForId2: FormError = FormError(key = "id2", message = "error.somethingElse", args = Seq.empty)

  private def createForm(errors: Seq[FormError] = Seq.empty) = new Form(
    mapping = formMappingWithoutMandatory,
    data = Map.empty,
    errors = errors,
    value = None
  )
}
