package filters

import com.google.inject.Inject
import common.ClientSideSessionFactory
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

class EnsureSessionCreatedFilter @Inject()(sessionFactory: ClientSideSessionFactory) extends Filter {
  def apply(nextFilter: (RequestHeader) => Future[SimpleResult])
           (requestHeader: RequestHeader): Future[SimpleResult] = {
    nextFilter(requestHeader).map { result =>
      sessionFactory.ensureSession(requestHeader.cookies, result)._1
    }
  }
}