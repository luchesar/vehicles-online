package services.dispose_service

/**
 * Created by valtechuk on 01/04/2014.
 */
trait DisposeWebService {
  def callVehicleLookupService(request: DisposeRequest): Future[Response]
}
