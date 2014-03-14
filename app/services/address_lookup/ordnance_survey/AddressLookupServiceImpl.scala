package services.address_lookup.ordnance_survey

import services.AddressLookupService
import models.domain.disposal_of_vehicle.AddressViewModel
import utils.helpers.Config
import play.api.Logger
import com.ning.http.client.Realm.AuthScheme
import services.address_lookup.ordnance_survey.domain.{OSAddressbaseDPA, OSAddressbaseResult, OSAddressbaseSearchResponse}
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

  // TODO write unit test that lists are sorted by building number
  // TODO write unit test that it does not throw when list contains a building number that contains letters.
  // TODO write unit test that lists are sorted by building number then building name
  def sort(addresses: Seq[OSAddressbaseDPA]) = {
    addresses.sortBy(addressDpa => {
      val buildingNumber = addressDpa.buildingNumber.getOrElse("0")
      val buildingNumberSanitised = if (buildingNumber.matches("[+-]?\\d+")) buildingNumber.toInt else 0 // Sanitise building number as it could contain letters which would cause toInt to throw e.g. 107a.
      (buildingNumberSanitised, addressDpa.buildingName) // TODO check with BAs how they would want to sort the list
    })
  }

  override def fetchAddressesForPostcode(postcode: String): Future[Seq[(String, String)]] = {
    def toUprnsAndAddresses(resp: Response): Seq[(String, String)] =
      extractFromJson(resp) match {
        case Some(results) =>
          val addresses = results.flatMap { _.DPA }
          sort(addresses) map { address => (address.UPRN, address.address) } // Sort before translating to view models.
        case None =>
          // Handle no results
          Logger.debug(s"No results returned for postcode: $postcode")
          Seq.empty
      }

    callPostcodeWebService(postcode).map { resp =>
      Logger.debug(s"Http response code from Ordnance Survey postcode lookup service was: ${resp.status}")
      if (resp.status == play.api.http.Status.OK) toUprnsAndAddresses(resp)
      else Seq.empty // The service returned http code other than 200
    }.recover {
      case e: Throwable =>
        Logger.error(s"Ordnance Survey postcode lookup service error: $e")
        Seq.empty
    }
  }

  override def fetchAddressForUprn(uprn: String): Future[Option[AddressViewModel]] = {
    // Extract result from response and return as a view model.
    def toAddressViewModel(resp: Response) =
      extractFromJson(resp) match {
        case Some(results) =>
          val addresses = results.flatMap { _.DPA }
          val addressViewModels = sort(addresses) map { address => AddressViewModel(uprn = Some(address.UPRN.toLong), address = address.address.split(", ")) } // Sort before translating to view models.
          require(addressViewModels.length >= 1, s"Should be at least one address for the UPRN: $uprn")
          Some(addressViewModels.head)
        case None =>
          Logger.error(s"No results returned by web service for submitted UPRN: $uprn")
          None
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
