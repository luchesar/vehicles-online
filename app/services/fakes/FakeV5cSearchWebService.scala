package services.fakes

import services.V5cSearchWebService
import models.domain.change_of_address.{V5cSearchConfirmationModel, V5cSearchResponse, V5cSearchModel}
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

class FakeV5cSearchWebService extends V5cSearchWebService {
  override def invoke(cmd: V5cSearchModel): Future[V5cSearchResponse] = Future {
    V5cSearchResponse(success = true, "All ok ", V5cSearchConfirmationModel("vrn", "make", "model", "firstRegistered", "acquired"))
  }
}