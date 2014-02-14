package services

import models.domain.change_of_address.{V5cSearchResponse, V5cSearchModel}

import scala.concurrent.Future

trait V5cSearchWebService {
  def invoke(cmd: V5cSearchModel): Future[V5cSearchResponse]
}
