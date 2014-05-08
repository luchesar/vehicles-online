package controllers.disposal_of_vehicle

import play.api.Logger
import models.domain.disposal_of_vehicle._
import models.domain.disposal_of_vehicle.{DealerDetailsModel, DisposeFormModel, SetupTradeDetailsModel, VehicleDetailsModel}
import scala.Some
import mappings.disposal_of_vehicle.SetupTradeDetails._
import mappings.disposal_of_vehicle.DealerDetails._
import mappings.disposal_of_vehicle.Dispose._
import mappings.disposal_of_vehicle.VehicleLookup._
import play.api.mvc.{Request, Cookie, SimpleResult}
import play.api.libs.json.{Writes, Reads, JsPath, Json}
import play.api.data.validation.ValidationError
import utils.helpers.CryptoHelper
import models.domain.common.CacheKey

case class JsonValidationException(errors: Seq[(JsPath, Seq[ValidationError])]) extends Exception

object DisposalOfVehicleSessionState {

  // TODO: This is the new way of doing caching. Remove old version piece by piece into the new style then rename this.
  implicit class RequestAdapter[A](val request: Request[A]) extends AnyVal {
    def fetch[B](implicit fjs: Reads[B], cacheKey: CacheKey[B]): Option[B] =
      request.cookies.get(CryptoHelper.encryptCookieName(cacheKey.value)) match {
        case Some(cookie) =>
          val decrypted = CryptoHelper.decryptCookie(cookie.value)
          val parsed = Json.parse(decrypted)
          val fromJson = Json.fromJson[B](parsed)
          fromJson.asEither match {
            case Left(errors) => throw JsonValidationException(errors)
            case Right(model) => Some(model)
          }
        case None => None
      }

    def fetch(key: String): Option[String] =
      request.cookies.get(CryptoHelper.encryptCookieName(key)) match {
        case Some(cookie) => Some(CryptoHelper.decryptCookie(cookie.value))
        case None => None
      }
  }

  implicit class SimpleResultAdapter(val result: SimpleResult) extends AnyVal {
    def withCookie[A](model: A)(implicit tjs: Writes[A], cacheKey: CacheKey[A]): SimpleResult = {
      val stateAsJson = Json.toJson(model)
      val encryptedStateAsJson = CryptoHelper.encryptCookie(stateAsJson.toString())
      val cookie = Cookie(CryptoHelper.encryptCookieName(cacheKey.value), encryptedStateAsJson)
      result.withCookies(cookie)
    }

    def withCookie(key: String, value: String): SimpleResult = {
      val encrypted = CryptoHelper.encryptCookie(value)
      val cookie = Cookie(CryptoHelper.encryptCookieName(key), encrypted)
      result.withCookies(cookie)
    }
  }
}