package services

import models.domain.change_of_address.{V5cSearchConfirmationModel, V5cSearchResponse, V5cSearchModel}
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.Logger
import scala.concurrent.Future

/**
 * Defines the V5CSearchWebService 
 */
trait WebService {
  def invoke(cmd: V5cSearchModel): Future[V5cSearchResponse]
}

