package controllers.disposal_of_vehicle

import com.google.inject.Inject
import common.ClientSideSessionFactory
import common.CookieImplicits.{FormAdapter, RequestCookiesAdapter, SimpleResultAdapter}
import constraints.common.DayMonthYear._
import mappings.common.Consent._
import mappings.common.DayMonthYear.dayMonthYear
import mappings.common.Interstitial.InterstitialCacheKey
import mappings.common.Mileage._
import mappings.disposal_of_vehicle.Dispose._
import models.domain.disposal_of_vehicle._
import org.joda.time.format.ISODateTimeFormat
import play.api.Logger
import play.api.data.Forms._
import play.api.data.{Form, FormError}
import play.api.mvc._
import services.DateService
import services.dispose_service.DisposeService
import utils.helpers.FormExtensions._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps

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

  def present = Action {
    implicit request => {
      (request.cookies.getModel[TraderDetailsModel], request.cookies.getString(InterstitialCacheKey)) match {
        case (Some(dealerDetails), None) =>
          // Pre-populate the form so that the consent checkbox is ticked and today's date is displayed in the date control
          request.cookies.getModel[VehicleDetailsModel] match {
            case (Some(vehicleDetails)) => Ok(views.html.disposal_of_vehicle.dispose(populateModelFromCachedData(dealerDetails, vehicleDetails), form.fill(), dateService))
            case _ => Redirect(routes.VehicleLookup.present())
          }
        case (_, Some(interstitial)) =>
          // US320 Kick them back to the VehicleLookup page if they arrive here by any route other that clicking the
          // "Exit" or "New Dispose" buttons.
          Redirect(routes.VehicleLookup.present()).
            discardingCookie(InterstitialCacheKey)
        case _ => Redirect(routes.SetUpTradeDetails.present())
      }
    }
  }

  def submit = Action.async {
    implicit request =>
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
                BadRequest(views.html.disposal_of_vehicle.dispose(disposeViewModel, formWithReplacedErrors, dateService))
              case _ =>
                Logger.debug("could not find expected data in cache on dispose submit - now redirecting...")
                Redirect(routes.SetUpTradeDetails.present())
            }
          },
        f => {
          request.cookies.getString(InterstitialCacheKey) match {
            case Some(_) => Future {
              Redirect(routes.VehicleLookup.present())
            } // US320 prevent user using the browser back button and resubmitting.
            case None =>
              val trackingId = request.cookies.trackingId()
              disposeAction(webService, f, trackingId)
          }
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
    //Logger.debug(s"Dispose page read the following data from cache: $model")
    model
  }

  private def disposeAction(webService: DisposeService, disposeFormModel: DisposeFormModel, trackingId: String)(implicit request: Request[AnyContent]): Future[SimpleResult] = {
    def callMicroService(disposeModel: DisposeModel, traderDetailsModel: TraderDetailsModel, trackingId: String) = {
      def nextPage(httpResponseCode: Int, response: Option[DisposeResponse]) = {
        // This makes the choice of which page to go to based on the first one it finds that is not None.
        response match {
          case Some(r) if r.responseCode.isDefined => handleResponseCode(r.responseCode.get)
          case _ => handleHttpStatusCode(httpResponseCode)
        }
      }

      val disposeRequest = buildDisposeMicroServiceRequest(disposeModel, traderDetailsModel)
      webService.invoke(disposeRequest, trackingId).map {
        case (httpResponseCode, response) => //Logger.debug(s"Dispose micro-service call successful response = $response") //ToDo Do we need to log this information?

          Some(Redirect(nextPage(httpResponseCode, response))).
            map(_.withCookie(disposeModel)).
            map(_.withCookie(disposeFormModel)).
            map(storeResponseInCache(response, _)).
            map(transactionTimestamp).
            map(_.withCookie(InterstitialCacheKey, routes.DisposeSuccess.present().url)). // US320 interstitial should redirect to DisposeSuccess.
            get
      }.recover {
        case e: Throwable =>
          Logger.warn(s"Dispose micro-service call failed. Exception: " + e.toString.take(45))
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
        traderAddress = DisposalAddressDto.from(traderDetails.traderAddress),
        dateOfDisposal = isoDateTimeString,
        transactionTimestamp = ISODateTimeFormat.dateTime().print(dateService.today.toDateTime.get),
        prConsent = disposeModel.lossOfRegistrationConsent.toBoolean,
        keeperConsent = disposeModel.consent.toBoolean,
        mileage = disposeModel.mileage)
    }

    def handleResponseCode(disposeResponseCode: String): Call = {
      disposeResponseCode match {
        case "ms.vehiclesService.response.unableToProcessApplication" =>
          Logger.warn("Dispose soap endpoint redirecting to dispose failure page")
          routes.DisposeFailure.present()
        case "ms.vehiclesService.response.duplicateDisposalToTrade" =>
          Logger.warn("Dispose soap endpoint redirecting to duplicate disposal page")
          routes.DuplicateDisposalError.present()
        case _ =>
          Logger.warn(s"Dispose micro-service failed redirecting to error page $disposeResponseCode")
          routes.MicroServiceError.present()
      }
    }

    def handleHttpStatusCode(statusCode: Int): Call = {
      statusCode match {
        case OK => routes.Interstitial.present()
        case SERVICE_UNAVAILABLE => routes.SoapEndpointError.present()
        case _ => routes.MicroServiceError.present()
      }
    }

    (request.cookies.getModel[TraderDetailsModel], request.cookies.getModel[VehicleLookupFormModel]) match {
      case (Some(traderDetails), Some(vehicleLookup)) =>
        val disposeModel = DisposeModel(referenceNumber = vehicleLookup.referenceNumber,
          registrationNumber = vehicleLookup.registrationNumber,
          dateOfDisposal = disposeFormModel.dateOfDisposal, consent = disposeFormModel.consent, lossOfRegistrationConsent = disposeFormModel.lossOfRegistrationConsent,
          mileage = disposeFormModel.mileage)
        callMicroService(disposeModel, traderDetails, trackingId)
      case (None, _) => Future {
        Logger.error("Could not find dealer details in cache on Dispose submit")
        Redirect(routes.SetUpTradeDetails.present())
      }
      case (_, None) => Future {
        Logger.error("Could not find vehicle details in cache on Dispose submit")
        Redirect(routes.SetUpTradeDetails.present())
      }
    }
  }
}
