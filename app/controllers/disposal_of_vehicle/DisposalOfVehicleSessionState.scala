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

  final lazy val SaltKey = CryptoHelper.encryptCookieName("FE291934-66BD-4500-B27F-517C7D77F26B")

  implicit class RequestAdapter[A](val request: Request[A]) extends AnyVal {
    def getCookie[B](implicit fjs: Reads[B], cacheKey: CacheKey[B]): Option[B] = {
      val salt = getSalt(request).getOrElse("")
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
      val salt = getSalt(request).getOrElse("")
      request.cookies.get(CryptoHelper.encryptCookieName(salt + key)).map { cookie =>
        CryptoHelper.decryptCookie(cookie.value)
      }
    }
  }

  implicit class SimpleResultAdapter(val result: SimpleResult) extends AnyVal {

    def withCookie[A](model: A)(implicit tjs: Writes[A], cacheKey: CacheKey[A], request: Request[_]): SimpleResult = {

      def withModel(resultWithSalt: (SimpleResult, String)): SimpleResult = {
        val (result, salt) = resultWithSalt
        val stateAsJson = Json.toJson(model)
        val encryptedStateAsJson = CryptoHelper.encryptCookie(stateAsJson.toString())
        val cookie = Cookie(CryptoHelper.encryptCookieName(salt + cacheKey.value), encryptedStateAsJson)
        result.withCookies(cookie)
      }

      Some(result)
        .map(withSalt)
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

      Some(result)
        .map(withSalt)
        .map(withKeyValuePair)
        .get
    }

    private def withSalt(result: SimpleResult)(implicit request: Request[_]): (SimpleResult, String) =
      getSalt(request) match {
        case Some(saltFromRequest) =>
          (result, saltFromRequest)
        case None =>
          val newSalt = CryptoHelper.newCookieNameSalt
          if (newSalt.isEmpty)
            (result, newSalt)
          else {
            val newSaltCookie = Cookie(SaltKey, CryptoHelper.encryptCookie(newSalt))
            val resultWithSalt = result.withCookies(newSaltCookie)
            (resultWithSalt, newSalt)
          }
      }
  }

  implicit class FormAdapter[A](val f: Form[A]) extends AnyVal {
    def fill()(implicit request: Request[_], fjs: Reads[A], cacheKey: CacheKey[A]): Form[A] =
      request.getCookie[A] match {
        case Some(v) => f.fill(v) // Found cookie so fill the form with the cached data.
        case _ => f // No cookie found so return a blank form.
      }
  }

  private def getSalt(request: Request[_]): Option[String] =
    request.cookies.get(SaltKey) match {
      case Some(cookie) => Some(CryptoHelper.decryptCookie(cookie.value))
      case None => None
    }
}
