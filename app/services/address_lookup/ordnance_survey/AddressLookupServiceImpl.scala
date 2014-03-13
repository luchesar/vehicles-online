package services.address_lookup.ordnance_survey

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

class AddressLookupServiceImpl @Inject()(ws: services.WebService) extends AddressLookupService {
  val username = s"${Config.ordnanceSurveyUsername}"
  val password = s"${Config.ordnanceSurveyPassword}"
  val baseUrl = s"${Config.ordnanceSurveyBaseUrl}"

  override protected def callPostcodeWebService(postcode: String): Future[Response] = {
    val endPoint = s"$baseUrl/postcode?postcode=${postcodeWithNoSpaces(postcode)}&dataset=dpa" // TODO add lpi to URL, but need to set organisation as Option on the type.
    Logger.debug(s"Calling Ordnance Survey postcode lookup service on $endPoint...")
    ws.url(endPoint).
      withAuth(username = username, password = password, scheme = AuthScheme.BASIC).
      withRequestTimeout(30000). // Timeout is in milliseconds // TODO Timeout value should be read from config file
      get()
  }

  override protected def callUprnWebService(uprn: String): Future[Response] = {
    val endPoint = s"$baseUrl/uprn?uprn=$uprn&dataset=dpa" // TODO add lpi to URL, but need to set orgnaisation as Option on the type.
    Logger.debug(s"Calling Ordnance Survey uprn lookup service on $endPoint...")
    ws.url(endPoint).
      withAuth(username = username, password = password, scheme = AuthScheme.BASIC).
      withRequestTimeout(30000). // Timeout is in milliseconds // TODO Timeout value should be read from config file
      get()
  }

  def extractFromJson(resp: Response): Option[Seq[OSAddressbaseResult]] = {
    val response = resp.json.asOpt[OSAddressbaseSearchResponse]
    response.flatMap(_.results)
  }

  override def fetchAddressesForPostcode(postcode: String): Future[Seq[(String, String)]] = {
    def toUprnsAndAddresses(resp: Response): Seq[(String, String)] = {
      extractFromJson(resp) match {
        case Some(r) =>
          r.flatMap { address =>
            address.DPA match {
              case Some(dpa) => Some((dpa.UPRN, dpa.address))
              case _ => None // TODO check if an LPI entry is present
            }
          }
        case None =>
          // Handle no results
          Logger.debug(s"No results returned for postcode: $postcode")
          Seq.empty
      }
    }

    callPostcodeWebService(postcode).map { resp =>
      Logger.debug(s"Http response code from Ordnance Survey postcode lookup service was: ${resp.status}")
      if (resp.status == play.api.http.Status.OK) toUprnsAndAddresses(resp).sortBy(x => x._1) // Sort by UPRN. TODO check with BAs how they would want to sort the list
      else Seq.empty // The service returned http code other than 200
    }.recover {
      case e: Throwable =>
        Logger.error(s"Ordnance Survey postcode lookup service error: $e")
        Seq.empty
    }
  }

  def postcodeWithNoSpaces(postcode: String): String = postcode.filter(_ != ' ')

  override def fetchAddressForUprn(uprn: String): Future[Option[AddressViewModel]] = {
    // Extract result from response and return as a view model.
    def toAddressViewModel(resp: Response) = {

      extractFromJson(resp) match {
        case Some(results) =>
          val addressViewModels = results.flatMap { address =>
            address.DPA match {
              case Some(dpa) => Some(AddressViewModel(uprn = Some(dpa.UPRN.toLong), address = dpa.address.split(",")))
              case _ => None // TODO check if an LPI entry is present
            }
          }
          require(addressViewModels.length >= 1, s"Should be at least one address for the UPRN: $uprn")
          Some(addressViewModels.head)
        case None =>
          Logger.error(s"No results returned by web service for submitted UPRN: $uprn")
          None
      }
    }

    callUprnWebService(uprn).map { resp =>
      Logger.debug(s"Http response code from Ordnance Survey uprn lookup service was: ${resp.status}")
      if (resp.status == play.api.http.Status.OK) toAddressViewModel(resp)
      else None
    }.recover {
      case e: Throwable =>
        Logger.error(s"Ordnance Survey uprn lookup service error: $e")
        None
    }
  }
}
