package services.address_lookup.gds

import services.AddressLookupService
import models.domain.disposal_of_vehicle.AddressViewModel
import utils.helpers.Config
import play.api.Logger
import com.ning.http.client.Realm.AuthScheme
import services.address_lookup.ordnance_survey.domain.{OSAddressbaseResult, OSAddressbaseSearchResponse}
import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global
import javax.inject.Inject
import play.api.libs.ws.Response
import services.address_lookup.gds.domain.Address

class AddressLookupServiceImpl @Inject()(ws: services.WebService) extends AddressLookupService {
  val username = s"${Config.ordnanceSurveyUsername}"
  val password = s"${Config.ordnanceSurveyPassword}"
  val baseUrl = s"${Config.ordnanceSurveyBaseUrl}"

  override protected def callPostcodeWebService(postcode: String): Future[Response] = ???

  override protected def callUprnWebService(uprn: String): Future[Response] = ???

  def extractFromJson(resp: Response): Option[Seq[Address]] = ???

  override def fetchAddressesForPostcode(postcode: String): Future[Seq[(String, String)]] = {


    callPostcodeWebService(postcode).map { resp =>
      Logger.debug(s"Http response code from Ordnance Survey postcode lookup service was: ${resp.status}")
      if (resp.status == play.api.http.Status.OK) ???
      else Seq.empty // The service returned http code other than 200
    }.recover {
      case e: Throwable =>
        Logger.error(s"Ordnance Survey postcode lookup service error: $e")
        Seq.empty
    }
  }

  def postcodeWithNoSpaces(postcode: String): String = postcode.filter(_ != ' ')

  override def fetchAddressForUprn(uprn: String): Future[Option[AddressViewModel]] = ???
}
