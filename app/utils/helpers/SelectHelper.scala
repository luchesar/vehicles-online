package utils.helpers

object SelectHelper {
  def defaultOption(htmlArgs: Map[Symbol, Any]) = htmlArgs.get('_default) match {
    case Some(defaultValue) => <option class="blank" value="">@defaultValue</option>
    case None =>
  }
}
