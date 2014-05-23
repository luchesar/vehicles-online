package common

import play.api.libs.json.{Writes, Reads, Json}
import play.api.mvc._
import play.api.data.Form
import models.domain.common.CacheKey
import scala.Some
import play.api.mvc.SimpleResult
import play.api.mvc.DiscardingCookie

object EncryptedCookieImplicits {

  implicit class RequestAdapter[A](val request: Request[A]) extends AnyVal {
    def getEncryptedCookie[B](implicit fjs: Reads[B], cacheKey: CacheKey[B], clientSideSessionFactory: ClientSideSessionFactory): Option[B] =
      for {
        session <- clientSideSessionFactory.getSession(request)
        cookieName <- Some(session.nameCookie(cacheKey.value).value)
        cookie <- request.cookies.get(cookieName)
      } yield {
        val json = session.getCookieValue(cookie)
        val parsed = Json.parse(json)
        val fromJson = Json.fromJson[B](parsed)
        fromJson.asEither match {
          case Left(errors) => throw JsonValidationException(errors)
          case Right(model) => model
        }
      }

    def getCookieNamed(key: String)(implicit clientSideSessionFactory: ClientSideSessionFactory): Option[String] =
      for {
        session <- clientSideSessionFactory.getSession(request)
        cookieName <- Some(session.nameCookie(key).value)
        cookie <- request.cookies.get(cookieName)
      } yield session.getCookieValue(cookie)
  }

  implicit class SimpleResultAdapter(val inner: SimpleResult) extends AnyVal {

    def withEncryptedCookie[A](model: A)(implicit
                                         tjs: Writes[A],
                                         cacheKey: CacheKey[A],
                                         request: Request[_],
                                         clientSideSessionFactory: ClientSideSessionFactory): SimpleResult = {
      val json = Json.toJson(model).toString()
      withEncryptedCookie(cacheKey.value, json)
    }

    def withEncryptedCookie(key: String, value: String)
                           (implicit request: Request[_], clientSideSessionFactory: ClientSideSessionFactory): SimpleResult = {
      val (newResult, session) = clientSideSessionFactory.ensureSession(request, inner)
      val cookieName = session.nameCookie(key)
      val cookie = session.newCookie(cookieName, value)
      newResult.withCookies(cookie)
    }

    def discardingEncryptedCookie(key: String)
                                 (implicit request: Request[_], clientSideSessionFactory: ClientSideSessionFactory): SimpleResult =
      discardingEncryptedCookies(Set(key))

    def discardingEncryptedCookies(keys: Set[String])
                                  (implicit request: Request[_], clientSideSessionFactory: ClientSideSessionFactory): SimpleResult =
      clientSideSessionFactory.getSession(request) match {
        case Some(session) =>
          val cookieNames = keys.map(session.nameCookie)
          val discardingCookies = cookieNames.map(cookieName => DiscardingCookie(cookieName.value)).toSeq
          inner.discardingCookies(discardingCookies: _*)
        case None =>
          inner
      }
  }

  implicit class FormAdapter[A](val f: Form[A]) extends AnyVal {
    def fill()(implicit request: Request[_], fjs: Reads[A], cacheKey: CacheKey[A], clientSideSessionFactory: ClientSideSessionFactory): Form[A] =
      request.getEncryptedCookie[A] match {
        case Some(v) => f.fill(v) // Found cookie so fill the form with the cached data.
        case _ => f // No cookie found so return a blank form.
      }
  }
}
