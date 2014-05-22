package common

import app.ConfigProperties._
import play.api.libs.json.{Writes, Reads, Json}
import play.api.mvc._
import utils.helpers.{CookieNameHashing, CookieEncryption, CryptoHelper}
import play.api.data.Form
import models.domain.common.CacheKey
import scala.Some
import play.api.mvc.SimpleResult
import play.api.mvc.DiscardingCookie

object EncryptedCookieImplicits {

  private val encryptCookies = getProperty("encryptCookies", default = true)

  implicit class RequestAdapter[A](val request: Request[A]) extends AnyVal {
    def getEncryptedCookie[B](implicit fjs: Reads[B], cacheKey: CacheKey[B], encryption: CookieEncryption, cookieNameHashing: CookieNameHashing): Option[B] = {
      val sessionSecretKey = CryptoHelper.getSessionSecretKeyFromRequest(request).getOrElse("")
      val hashedCookieName = cookieNameHashing.hash(sessionSecretKey + cacheKey.value)
      request.cookies.get(hashedCookieName).map { cookie =>
        val decrypted = encryption.decrypt(cookie.value)
        val parsed = getJsonValue(decrypted, hashedCookieName)
        val fromJson = Json.fromJson[B](parsed)
        fromJson.asEither match {
          case Left(errors) => throw JsonValidationException(errors)
          case Right(model) => model
        }
      }
    }

    private def getJsonValue(decrypted: String, hashedCookieName: String): play.api.libs.json.JsValue = {
      if (encryptCookies) {
        val cookieNameFromPayload = decrypted.substring(0, CryptoHelper.CookieNameFromPayloadSize)
        assert(cookieNameFromPayload == hashedCookieName, "The cookie name bytes from the payload must match the cookie name")
        Json.parse(decrypted.substring(CryptoHelper.CookieNameFromPayloadSize))
      }
      else
        Json.parse(decrypted)
    }

    def getCookieNamed(key: String)(implicit encryption: CookieEncryption, cookieNameHashing: CookieNameHashing): Option[String] = {
      val sessionSecretKey = CryptoHelper.getSessionSecretKeyFromRequest(request).getOrElse("")
      request.cookies.get(cookieNameHashing.hash(sessionSecretKey + key)).map { cookie =>
        encryption.decrypt(cookie.value)
      }
    }
  }

  implicit class SimpleResultAdapter(val inner: SimpleResult) extends AnyVal {

    def withEncryptedCookie[A](model: A)(implicit tjs: Writes[A], cacheKey: CacheKey[A], request: Request[_],
                                         encryption: CookieEncryption, cookieNameHashing: CookieNameHashing): SimpleResult = {

      def withModel(resultWithSessionSecretKey: (SimpleResult, String)): SimpleResult = {
        val (result, sessionSecretKey) = resultWithSessionSecretKey
        val stateAsJson = Json.toJson(model)
        val hashedCookieName = cookieNameHashing.hash(sessionSecretKey + cacheKey.value)
        val encryptedStateAsJson =
          if (encryptCookies) encryption.encrypt(hashedCookieName + stateAsJson.toString())
          else encryption.encrypt(stateAsJson.toString())
        val cookie = CryptoHelper.createCookie(name = hashedCookieName, value = encryptedStateAsJson)
        result.withCookies(cookie)
      }

      Some(inner)
        .map(CryptoHelper.ensureSessionSecretKeyInResult)
        .map(withModel)
        .get
    }

    def discardingEncryptedCookie(keyUnhashed: String)(implicit request: Request[_], encryption: CookieEncryption,
                                               cookieNameHashing: CookieNameHashing): SimpleResult = {
      val sessionSecretKey = CryptoHelper.getSessionSecretKeyFromRequest(request).getOrElse("")
      val cookieToDiscard =  {
        val cookieNameHashed = cookieNameHashing.hash(sessionSecretKey + keyUnhashed) // We are selectively removing a cookie, so we need to convert the key to a hashed form to do the matching.
        DiscardingCookie(name = cookieNameHashed)
      }
      inner.discardingCookies(cookieToDiscard)
    }

    def discardingEncryptedCookies(keysUnhashed: Set[String])(implicit request: Request[_], encryption: CookieEncryption,
                                                      cookieNameHashing: CookieNameHashing): SimpleResult = {
      val sessionSecretKey = CryptoHelper.getSessionSecretKeyFromRequest(request).getOrElse("")
      val cookiesToDiscard = keysUnhashed.map(cookieName => {
        val cookieNameHashed = cookieNameHashing.hash(sessionSecretKey + cookieName) // We are selectively removing cookies, so we need to convert the key to a hashed form to do the matching.
        DiscardingCookie(name = cookieNameHashed)
      }).toSeq
      inner.discardingCookies(cookiesToDiscard: _*)
    }

    def discardingEncryptedCookies(keysHashed: Set[String], request: RequestHeader): SimpleResult = {
      val cookiesToDiscard = keysHashed.map(cookieName => DiscardingCookie(name = cookieName)).toSeq
      inner.discardingCookies(cookiesToDiscard: _*)
    }

    def withEncryptedCookie(key: String, value: String)(implicit request: Request[_], encryption: CookieEncryption,
                                                        cookieNameHashing: CookieNameHashing): SimpleResult = {

      def withKeyValuePair(resultWithSessionSecretKey: (SimpleResult, String)): SimpleResult = {
        val (result, sessionSecretKey) = resultWithSessionSecretKey
        val hashedCookieName = cookieNameHashing.hash(sessionSecretKey + key)
        val encrypted = if (encryptCookies) encryption.encrypt(hashedCookieName + value) else encryption.encrypt(value)
        val cookie = CryptoHelper.createCookie(name = hashedCookieName,
          value = encrypted)
        result.withCookies(cookie)
      }

      Some(inner)
        .map(CryptoHelper.ensureSessionSecretKeyInResult)
        .map(withKeyValuePair)
        .get
    }
  }

  implicit class FormAdapter[A](val f: Form[A]) extends AnyVal {
    def fill()(implicit request: Request[_], fjs: Reads[A], cacheKey: CacheKey[A], encryption: CookieEncryption, hashing: CookieNameHashing): Form[A] =
      request.getEncryptedCookie[A] match {
        case Some(v) => f.fill(v) // Found cookie so fill the form with the cached data.
        case _ => f // No cookie found so return a blank form.
      }
  }

}
