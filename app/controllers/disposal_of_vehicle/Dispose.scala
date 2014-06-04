package controllers.disposal_of_vehicle

import _root_.common.{ClientSideSessionFactory, CookieImplicits}
import play.api.data.Form
import play.api.data.Forms._
import play.api.Logger
import play.api.mvc._
import mappings.disposal_of_vehicle.Dispose._
import mappings.common.Mileage._
import mappings.common.DayMonthYear.dayMonthYear
import mappings.common.Consent._
import constraints.common
import common.DayMonthYear._
import models.domain.disposal_of_vehicle._
import com.google.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import utils.helpers.FormExtensions._
import models.domain.disposal_of_vehicle.VehicleDetailsModel
import org.joda.time.format.ISODateTimeFormat
import services.dispose_service.DisposeService
import services.DateService
import models.domain.disposal_of_vehicle.DisposeFormModel
import models.domain.disposal_of_vehicle.TraderDetailsModel
import models.domain.disposal_of_vehicle.DisposeModel
import mappings.disposal_of_vehicle.Dispose.DateOfDisposalYearsIntoThePast
import scala.language.postfixOps
import CookieImplicits.RequestCookiesAdapter
import CookieImplicits.SimpleResultAdapter
import CookieImplicits.FormAdapter
import utils.helpers.{CookieNameHashing, CookieEncryption}
import scala.Some
import play.api.mvc.SimpleResult
import models.domain.disposal_of_vehicle.DisposeViewModel
import play.api.data.FormError
import play.api.mvc.Call
import mappings.common.AddressLines._
import scala.annotation.tailrec

final class Dispose @Inject()(webService: DisposeService, dateService: DateService)(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {

  private[disposal_of_vehicle] val form = Form(
    mapping(
      MileageId -> mileage(),
      DateOfDisposalId -> dayMonthYear.verifying(validDate(),
        after(earliest = dateService.today - DateOfDisposalYearsIntoThePast years),
        notInFuture(dateService)),
      ConsentId -> consent,
      LossOfRegistrationConsentId -> consent
    )(DisposeFormModel.apply)(DisposeFormModel.unapply)
  )

  private val yearsDropdown: Seq[(String, String)] = dateService.today.yearRangeToDropdown(yearsIntoThePast = DateOfDisposalYearsIntoThePast)

  def present = Action {
    implicit request => {
      request.cookies.getModel[TraderDetailsModel] match {
        case (Some(dealerDetails)) =>
          Logger.debug("found dealer details")
          // Pre-populate the form so that the consent checkbox is ticked and today's date is displayed in the date control
          request.cookies.getModel[VehicleDetailsModel] match {
            case (Some(vehicleDetails)) => Ok(views.html.disposal_of_vehicle.dispose(populateModelFromCachedData(dealerDetails, vehicleDetails), form.fill(), yearsDropdown))
            case _ => Redirect(routes.VehicleLookup.present())
          }
        case _ => Redirect(routes.SetUpTradeDetails.present())
      }
    }
  }

  def submit = Action.async {
    implicit request =>
      Logger.debug("Submitted dispose form...")
      form.bindFromRequest.fold(
        formWithErrors =>
          Future {
            (request.cookies.getModel[TraderDetailsModel], request.cookies.getModel[VehicleDetailsModel]) match {
              case (Some(dealerDetails), Some(vehicleDetails)) =>
                val disposeViewModel = populateModelFromCachedData(dealerDetails, vehicleDetails)
                // When the user doesn't select a value from the drop-down then the mapping will fail to match on an Int before
                // it gets to the constraints, so we need to replace the error type with one that will give a relevant message.
                val formWithReplacedErrors = formWithErrors.
                  replaceError("dateOfDisposal.day", FormError("dateOfDisposal", "error.dateOfDisposal")).
                  replaceError("dateOfDisposal.month", FormError("dateOfDisposal", "error.dateOfDisposal")).
                  replaceError("dateOfDisposal.year", FormError("dateOfDisposal", "error.dateOfDisposal")).
                  replaceError("dateOfDisposal", FormError("dateOfDisposal", "error.dateOfDisposal")).
                  replaceError(ConsentId, "error.required", FormError(key = ConsentId, message = "disposal_dispose.consent.notgiven", args = Seq.empty)).
                  replaceError(LossOfRegistrationConsentId, "error.required", FormError(key = LossOfRegistrationConsentId, message = "disposal_dispose.loss_of_registration.consent.notgiven", args = Seq.empty)).
                  distinctErrors
                BadRequest(views.html.disposal_of_vehicle.dispose(disposeViewModel, formWithReplacedErrors, yearsDropdown))
              case _ =>
                Logger.debug("could not find expected data in cache on dispose submit - now redirecting...")
                Redirect(routes.SetUpTradeDetails.present())
            }
          },
        f => {
          Logger.debug(s"Dispose form submitted - mileage = ${f.mileage}, disposalDate = ${f.dateOfDisposal}, consent=${f.consent}, lossOfRegistrationConsent=${f.lossOfRegistrationConsent}")
          disposeAction(webService, f)
        }
      )
  }

  private def populateModelFromCachedData(dealerDetails: TraderDetailsModel, vehicleDetails: VehicleDetailsModel): DisposeViewModel = {
    val model = DisposeViewModel(
      registrationNumber = vehicleDetails.registrationNumber,
      vehicleMake = vehicleDetails.vehicleMake,
      vehicleModel = vehicleDetails.vehicleModel,
      dealerName = dealerDetails.traderName,
      dealerAddress = dealerDetails.traderAddress)
    Logger.debug(s"Dispose page read the following data from cache: $model")
    model
  }

  private def disposeAction(webService: DisposeService, disposeFormModel: DisposeFormModel)(implicit request: Request[AnyContent]): Future[SimpleResult] = {
    def callMicroService(disposeModel: DisposeModel, traderDetailsModel: TraderDetailsModel) = {
      def nextPage(httpResponseCode: Int, response: Option[DisposeResponse]) = {
        // This makes the choice of which page to go to based on the first one it finds that is not None.
        response match {
          case Some(r) if r.responseCode.isDefined => handleResponseCode(r.responseCode.get)
          case _ => handleHttpStatusCode(httpResponseCode)
        }
      }

      val disposeRequest = buildDisposeMicroServiceRequest(disposeModel, traderDetailsModel)
      webService.invoke(disposeRequest).map {
        case (httpResponseCode, response) => Logger.debug(s"Dispose micro-service call successful - response = $response")

         Some(Redirect(nextPage(httpResponseCode, response))).
            map(_.withCookie(disposeModel)).
            map(_.withCookie(disposeFormModel)).
            map(storeResponseInCache(response, _)).
            map(transactionTimestamp).
            get
      }.recover {
        case e: Throwable =>
          Logger.warn(s"Dispose micro-service call failed. Exception: $e")
          Redirect(routes.MicroServiceError.present())
      }
    }

    def storeResponseInCache(response: Option[DisposeResponse], nextPage: SimpleResult): SimpleResult = {
      response match {
        case Some(o) =>
          val nextPageWithTransactionId = if (o.transactionId != "") nextPage.withCookie(DisposeFormTransactionIdCacheKey, o.transactionId)
            else nextPage
          if (o.registrationNumber != "") nextPageWithTransactionId.withCookie(DisposeFormRegistrationNumberCacheKey, o.registrationNumber)
            else nextPageWithTransactionId
        case None => nextPage
      }
    }

      def transactionTimestamp(nextPage: SimpleResult) = {
      val transactionTimestamp = dateService.today.toDateTime.get
      val formatter = ISODateTimeFormat.dateTime()
      val isoDateTimeString = formatter.print(transactionTimestamp)
      nextPage.withCookie(DisposeFormTimestampIdCacheKey, isoDateTimeString)
    }

    def buildDisposeMicroServiceRequest(disposeModel: DisposeModel, traderDetails: TraderDetailsModel): DisposeRequest = {
      val dateTime = disposeModel.dateOfDisposal.toDateTime.get
      val formatter = ISODateTimeFormat.dateTime()
      val isoDateTimeString = formatter.print(dateTime)

      DisposeRequest(referenceNumber = disposeModel.referenceNumber,
        registrationNumber = disposeModel.registrationNumber,
        traderName = traderDetails.traderName,
        traderAddress = disposalAddressDto(traderDetails.traderAddress),
        dateOfDisposal = isoDateTimeString,
        transactionTimestamp = ISODateTimeFormat.dateTime().print(dateService.today.toDateTime.get),
        prConsent = disposeModel.lossOfRegistrationConsent.toBoolean,
        keeperConsent = disposeModel.consent.toBoolean,
        trackingId = request.cookies.trackingId(),
        mileage = disposeModel.mileage)
    }

    def handleResponseCode(disposeResponseCode: String): Call = {
      disposeResponseCode match {
        case "ms.vehiclesService.response.unableToProcessApplication" => {
          Logger.warn("Dispose soap endpoint redirecting to dispose failure page...")
          routes.DisposeFailure.present()
        }
        case "ms.vehiclesService.response.duplicateDisposalToTrade" => {
          Logger.warn("Dispose soap endpoint redirecting to duplicate disposal page...")
          routes.DuplicateDisposalError.present()
        }
        case _ => {
          Logger.warn(s"Dispose micro-service failed: $disposeResponseCode, redirecting to error page...")
          routes.MicroServiceError.present()
        }
      }
    }

    def handleHttpStatusCode(statusCode: Int): Call = {
      statusCode match {
        case OK => routes.DisposeSuccess.present()
        case SERVICE_UNAVAILABLE => routes.SoapEndpointError.present()
        case _ => routes.MicroServiceError.present()
      }
    }

    (request.cookies.getModel[TraderDetailsModel], request.cookies.getModel[VehicleLookupFormModel]) match {
      case (Some(traderDetails), Some(vehicleLookup)) =>  {
        val disposeModel = DisposeModel(referenceNumber = vehicleLookup.referenceNumber,
          registrationNumber = vehicleLookup.registrationNumber,
          dateOfDisposal = disposeFormModel.dateOfDisposal, consent = disposeFormModel.consent, lossOfRegistrationConsent = disposeFormModel.lossOfRegistrationConsent,
          mileage = disposeFormModel.mileage)
        callMicroService(disposeModel, traderDetails)
      }
      case (None, _) => Future {
        Logger.error("could not find dealer details in cache on Dispose submit")
        Redirect(routes.SetUpTradeDetails.present())
      }
      case (_, None) =>  Future {
        Logger.error("could not find vehicle details in cache on Dispose submit")
        Redirect(routes.SetUpTradeDetails.present())
      }
    }
  }

  private def disposalAddressDto(sourceAddress: AddressViewModel): DisposalAddressDto = {

    val sourceAddressToCheck = assignEmptyLines(sourceAddress.address)

    val (line2Empty, line3Empty) = (sourceAddressToCheck(Line2Index) == emptyLine, sourceAddressToCheck(Line3Index) == emptyLine)
    val (line1OverMax, line2OverMax) = (sourceAddressToCheck(BuildingNameOrNumberIndex).size > LineMaxLength, sourceAddressToCheck(Line2Index).size > LineMaxLength)

    val sourceAddressAmendedLines = addressLinesOverMaxToEmptyLines(line1OverMax, line2OverMax, line2Empty, line3Empty, sourceAddressToCheck)

    val legacyAddressLines = lineLengthCheck(sourceAddressAmendedLines.dropRight(2), Nil)
    val postTownToCheck = sourceAddressToCheck.takeRight(2).head
    val postTown = if (postTownToCheck.size > LineMaxLength) postTownToCheck.substring(0, LineMaxLength) else postTownToCheck
    val postcode = sourceAddress.address.last.replaceAll(" ","")

    //Logger.debug("DisposalAddressDto is " + legacyAddressLines + ", " + Some(postTown) + ", " + postcode + ", " + sourceAddress.uprn)
    DisposalAddressDto(legacyAddressLines, Some(postTown), postcode, sourceAddress.uprn)
  }

  private def assignEmptyLines(sourceAddress: Seq[String]) : Seq[String] = {
    sourceAddress.size match { //every address returned by OS contains at least one address line and a postcode
      case 2 => Seq(BuildingNameOrNumberHolder) ++ sourceAddress
      case 3 => Seq(sourceAddress(BuildingNameOrNumberIndex)) ++ Seq(emptyLine) ++ sourceAddress.tail
      case 4 => Seq(sourceAddress(BuildingNameOrNumberIndex)) ++ Seq(sourceAddress(Line2Index)) ++ Seq(emptyLine) ++ sourceAddress.tail.tail
      case _ => sourceAddress
    }
  }

  private def addressLinesOverMaxToEmptyLines(line1OverMax: Boolean, line2OverMax: Boolean, line2Empty: Boolean, line3Empty: Boolean, sourceAddressToCheck: Seq[String]) : Seq[String]= {
    (line1OverMax, line2OverMax, line2Empty, line3Empty) match {
      case (true, _, true, _) => Seq(sourceAddressToCheck(BuildingNameOrNumberIndex).substring(0, LineMaxLength)) ++
                                 Seq(sourceAddressToCheck(BuildingNameOrNumberIndex).substring(LineMaxLength)) ++
                                 sourceAddressToCheck.tail.tail
      case (true, _, false, true) => Seq(sourceAddressToCheck(BuildingNameOrNumberIndex).substring(0, LineMaxLength)) ++
                                     Seq(sourceAddressToCheck(BuildingNameOrNumberIndex).substring(LineMaxLength)) ++
                                     Seq(sourceAddressToCheck(Line2Index)) ++
                                     sourceAddressToCheck.tail.tail.tail
      case (false, true, false, true) => Seq(sourceAddressToCheck(BuildingNameOrNumberIndex)) ++
                                         Seq(sourceAddressToCheck(Line2Index).substring(0, LineMaxLength)) ++
                                         Seq(sourceAddressToCheck(Line2Index).substring(LineMaxLength)) ++
                                         sourceAddressToCheck.tail.tail.tail
      case (_) => sourceAddressToCheck
    }
  }

  @tailrec
  private def lineLengthCheck(existingAddress: Seq[String], accAddress: Seq[String]) : Seq[String] = {
    if (existingAddress.isEmpty) accAddress
    else if (existingAddress.head.size > LineMaxLength) lineLengthCheck(existingAddress.tail, accAddress :+ existingAddress.head.substring(0, LineMaxLength))
    else lineLengthCheck(existingAddress.tail, accAddress :+ existingAddress.head)
  }
}
