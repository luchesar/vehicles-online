package helpers

import play.api.libs.json.{Json, Reads}
import org.scalatest.Assertions.fail

object JsonUtils {

  def deserializeJsonToModel[A](json: String)(implicit fjs: Reads[A]): A = {
    val parsedJsValue = Json.parse(json)
    val fromJson = Json.fromJson[A](parsedJsValue)
    fromJson.asEither match {
      case Left(errors) =>
        fail(s"Failed to deserialize this json: $json into a model")
      case Right(model) =>
        model
    }
  }

}
