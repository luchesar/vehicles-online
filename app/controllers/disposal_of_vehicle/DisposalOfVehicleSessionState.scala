package controllers.disposal_of_vehicle

import play.api.libs.json.{Writes, Reads, JsPath, Json}
import play.api.mvc._
import utils.helpers.CryptoHelper
import models.domain.common.CacheKey
import play.api.mvc.Cookie
import scala.Some
import play.api.data.validation.ValidationError
import play.api.mvc.SimpleResult
import play.api.data.Form

case class JsonValidationException(errors: Seq[(JsPath, Seq[ValidationError])]) extends Exception

object DisposalOfVehicleSessionState {

  implicit class RequestAdapter[A](val request: Request[A]) extends AnyVal {
    def getCookie[B](implicit fjs: Reads[B], cacheKey: CacheKey[B]): Option[B] = {
      val salt = CryptoHelper.getSaltFromRequest(request).getOrElse("")
      request.cookies.get(CryptoHelper.encryptCookieName(salt + cacheKey.value)).map { cookie =>
        val decrypted = CryptoHelper.decryptCookie(cookie.value)
        val parsed = Json.parse(decrypted)
        val fromJson = Json.fromJson[B](parsed)
        fromJson.asEither match {
          case Left(errors) => throw JsonValidationException(errors)
          case Right(model) => model
        }
      }
    }

    def getCookieNamed(key: String): Option[String] = {
      val salt = CryptoHelper.getSaltFromRequest(request).getOrElse("")
      request.cookies.get(CryptoHelper.encryptCookieName(salt + key)).map { cookie =>
        CryptoHelper.decryptCookie(cookie.value)
      }
    }
  }

  implicit class SimpleResultAdapter(val inner: SimpleResult) extends AnyVal {

    def withCookie[A](model: A)(implicit tjs: Writes[A], cacheKey: CacheKey[A], request: Request[_]): SimpleResult = {

      def withModel(resultWithSalt: (SimpleResult, String)): SimpleResult = {
        val (result, salt) = resultWithSalt
        val stateAsJson = Json.toJson(model)
        val encryptedStateAsJson = CryptoHelper.encryptCookie(stateAsJson.toString())
        val cookie = Cookie(CryptoHelper.encryptCookieName(salt + cacheKey.value), encryptedStateAsJson)
        result.withCookies(cookie)
      }

      Some(inner)
        .map(CryptoHelper.ensureSaltInResult)
        .map(withModel)
        .get
    }

    def withCookie(key: String, value: String)(implicit request: Request[_]): SimpleResult = {

      def withKeyValuePair(resultWithSalt: (SimpleResult, String)): SimpleResult = {
        val (result, salt) = resultWithSalt
        val encrypted = CryptoHelper.encryptCookie(value)
        val cookie = Cookie(CryptoHelper.encryptCookieName(salt + key), encrypted)
        result.withCookies(cookie)
      }

      Some(inner)
        .map(CryptoHelper.ensureSaltInResult)
        .map(withKeyValuePair)
        .get
    }
  }

  implicit class FormAdapter[A](val f: Form[A]) extends AnyVal {
    def fill()(implicit request: Request[_], fjs: Reads[A], cacheKey: CacheKey[A]): Form[A] =
      request.getCookie[A] match {
        case Some(v) => f.fill(v) // Found cookie so fill the form with the cached data.
        case _ => f // No cookie found so return a blank form.
      }
  }
}
