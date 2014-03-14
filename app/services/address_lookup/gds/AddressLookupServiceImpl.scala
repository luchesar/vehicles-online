package services.address_lookup.gds

import services.AddressLookupService
import models.domain.disposal_of_vehicle.AddressViewModel
import utils.helpers.Config
import play.api.Logger
import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global
import javax.inject.Inject
import play.api.libs.ws.Response
import services.address_lookup.gds.domain.Address
import services.address_lookup.gds.domain.JsonFormats.addressFormat
import com.ning.http.client.Realm.AuthScheme
import services.address_lookup.ordnance_survey.domain.OSAddressbaseDPA

class AddressLookupServiceImpl @Inject()(ws: services.WebService) extends AddressLookupService {
  val baseUrl = Config.gdsAddressLookupBaseUrl
  val authorisation = Config.gdsAddressLookupAuthorisation
  val requestTimeout = Config.gdsAddressLookupRequestTimeout.toInt

  // request should look like    (GET, "/addresses?postcode=kt70ej").withHeaders(validAuthHeader)
  override protected def callPostcodeWebService(postcode: String): Future[Response] = {
    val endPoint = s"$baseUrl/addresses?postcode=${ postcodeWithNoSpaces(postcode) }" // TODO add lpi to URL, but need to set organisation as Option on the type.
    Logger.debug(s"Calling GDS postcode lookup service on $endPoint...")
    ws.url(endPoint).
      withHeaders("AUTHORIZATION" -> authorisation).
      withRequestTimeout(requestTimeout). // Timeout is in milliseconds // TODO Timeout value should be read from config file
      get()
  }

  override protected def callUprnWebService(uprn: String): Future[Response] = ???

  private def extractFromJson(resp: Response): Seq[Address] = {
    try resp.json.as[Seq[Address]]
    catch {
      case e: Throwable => Seq.empty //  return empty seq given invalid json
    }
  }

  override def fetchAddressesForPostcode(postcode: String): Future[Seq[(String, String)]] = {
    def sort(addresses: Seq[Address]) = {
      addresses.sortBy(addressDpa => {
        val buildingNumber = addressDpa.houseNumber.getOrElse("0")
        val buildingNumberSanitised = buildingNumber.replaceAll("[^0-9]", "") // Sanitise building number as it could contain letters which would cause toInt to throw e.g. 107a.
        (buildingNumberSanitised, addressDpa.houseName) // TODO check with BAs how they would want to sort the list
      })
    }

    def toDropDown(resp: Response): Seq[(String, String)] = {
      val addresses = extractFromJson(resp)
      sort(addresses) map { address => (address.presentation.uprn, address.toViewModel.mkString(", ")) } // Sort before translating to drop down format.
    }

    callPostcodeWebService(postcode).map {
      resp =>
        Logger.debug(s"Http response code from GDS postcode lookup service was: ${ resp.status }")
        if (resp.status == play.api.http.Status.OK) toDropDown(resp)
        else Seq.empty // The service returned http code other than 200
    }.recover {
      case e: Throwable => Seq.empty
    }
  }

  override def fetchAddressForUprn(uprn: String): Future[Option[AddressViewModel]] = {
    def toViewModel(resp: Response) = {
      val addresses = extractFromJson(resp)
      require(addresses.length >= 1, s"Should be at least one address for the UPRN: $uprn")
      Some(AddressViewModel(uprn = Some(addresses.head.presentation.uprn.toLong), address = addresses.head.toViewModel)) // Translate to view model.
    }

    callUprnWebService(uprn).map {
      resp =>
        Logger.debug(s"Http response code from GDS postcode lookup service was: ${ resp.status }")
        if (resp.status == play.api.http.Status.OK) toViewModel(resp)
        else None
    }.recover {
      case e: Throwable => {
        Logger.error(s"GDS uprn lookup service error: ${ e }")
        None
      }
    }
  }
}
