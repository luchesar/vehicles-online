package common

import play.api.libs.json.{Writes, Reads, Json}
import play.api.mvc._
import play.api.data.Form
import play.api.mvc.DiscardingCookie
import models.domain.common.CacheKey
import scala.Some
import play.api.mvc.SimpleResult
import play.api.i18n.Lang
import play.api.Play.current

object CookieImplicits {

  implicit class RequestCookiesAdapter[A](val requestCookies: Traversable[Cookie]) extends AnyVal {
    def getModel[B](implicit fjs: Reads[B], cacheKey: CacheKey[B], clientSideSessionFactory: ClientSideSessionFactory): Option[B] = {
      val session = clientSideSessionFactory.getSession(requestCookies)
      val cookieName = session.nameCookie(cacheKey.value).value
      requestCookies.find(_.name == cookieName).map { cookie =>
        val json = session.getCookieValue(cookie)
        val parsed = Json.parse(json)
        val fromJson = Json.fromJson[B](parsed)
        fromJson.asEither match {
          case Left(errors) =>
            throw JsonValidationException(errors)
          case Right(model) =>
            model
        }
      }
    }

    def getString(key: String)(implicit clientSideSessionFactory: ClientSideSessionFactory): Option[String] = {
      val session = clientSideSessionFactory.getSession(requestCookies)
      val cookieName = session.nameCookie(key).value
      requestCookies.find(_.name == cookieName).map(session.getCookieValue)
    }

    def trackingId()
      (implicit clientSideSessionFactory: ClientSideSessionFactory): String =
      clientSideSessionFactory.getSession(requestCookies).trackingId
  }

  implicit class SimpleResultAdapter(val inner: SimpleResult) extends AnyVal {

    def withCookie[A](model: A)(implicit
                                         tjs: Writes[A],
                                         cacheKey: CacheKey[A],
                                         request: Request[_],
                                         clientSideSessionFactory: ClientSideSessionFactory): SimpleResult = {
      val json = Json.toJson(model).toString()
      withCookie(cacheKey.value, json)
    }

    def withCookie(key: String, value: String)
                           (implicit request: Request[_], clientSideSessionFactory: ClientSideSessionFactory): SimpleResult = {
      val session = clientSideSessionFactory.getSession(request.cookies)
      val cookieName = session.nameCookie(key)
      val cookie = session.newCookie(cookieName, value)
      inner.withCookies(cookie)
    }

    def discardingCookie(key: String)
                                 (implicit request: Request[_], clientSideSessionFactory: ClientSideSessionFactory): SimpleResult =
      discardingCookies(Set(key))

    def discardingCookies(keys: Set[String])
                                  (implicit request: Request[_], clientSideSessionFactory: ClientSideSessionFactory): SimpleResult = {
      val session = clientSideSessionFactory.getSession(request.cookies)
      val cookieNames = keys.map(session.nameCookie)
      val discardingCookies = cookieNames.map(cookieName => DiscardingCookie(cookieName.value)).toSeq
      inner.discardingCookies(discardingCookies: _*)
    }
  }

  implicit class FormAdapter[A](val f: Form[A]) extends AnyVal {
    def fill()(implicit request: Request[_], fjs: Reads[A], cacheKey: CacheKey[A], clientSideSessionFactory: ClientSideSessionFactory): Form[A] =
      request.cookies.getModel[A] match {
        case Some(v) => f.fill(v) // Found cookie so fill the form with the cached data.
        case _ => f // No cookie found so return a blank form.
      }
  }
}
