package services.dispose_service

/**
 * Created by valtechuk on 01/04/2014.
 */
class DisposeWebServiceImpl extends DisposeWebService {
  val endPoint = s"${Config.microServiceBaseUrl}/vehicles/dispose/v1"

  def callVehicleLookupService(request: DisposeRequest): Future[Response] = {
    Logger.debug(s"Calling dispose vehicle micro service on $endPoint with request object: $request...")
    WS.url(endPoint).post(Json.toJson(request))
  }
}
