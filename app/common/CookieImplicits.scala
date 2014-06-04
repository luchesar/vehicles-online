package common

import play.api.libs.json.{Writes, Reads, Json}
import play.api.mvc._
import play.api.data.Form
import play.api.mvc.DiscardingCookie
import models.domain.common.CacheKey
import scala.Some
import play.api.mvc.SimpleResult
import play.api.Logger

object CookieImplicits {

  implicit class RequestCookiesAdapter[A](val requestCookies: Traversable[Cookie]) extends AnyVal {
    def getModel[B](implicit fjs: Reads[B], cacheKey: CacheKey[B], clientSideSessionFactory: ClientSideSessionFactory): Option[B] = {
      for {
        session <- clientSideSessionFactory.getSession(requestCookies)
        cookieName <- Some(session.nameCookie(cacheKey.value).value)
        cookie <- requestCookies.find(_.name == cookieName)
      } yield {
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

    def getString(key: String)(implicit clientSideSessionFactory: ClientSideSessionFactory): Option[String] =
      for {
        session <- clientSideSessionFactory.getSession(requestCookies)
        cookieName <- Some(session.nameCookie(key).value)
        cookie <- requestCookies.find(_.name == cookieName)
      } yield session.getCookieValue(cookie)

    def trackingId()
    (implicit clientSideSessionFactory: ClientSideSessionFactory): String =
      clientSideSessionFactory.getSession(requestCookies) match {
        case Some(s) => s.trackingId
        case _ => ""
    }
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
      val (newResult, session) = clientSideSessionFactory.ensureSession(request.cookies, inner)
      val cookieName = session.nameCookie(key)
      val cookie = session.newCookie(cookieName, value)
      newResult.withCookies(cookie)
    }

    def discardingCookie(key: String)
                                 (implicit request: Request[_], clientSideSessionFactory: ClientSideSessionFactory): SimpleResult =
      discardingCookies(Set(key))

    def discardingCookies(keys: Set[String])
                                  (implicit request: Request[_], clientSideSessionFactory: ClientSideSessionFactory): SimpleResult =
      clientSideSessionFactory.getSession(request.cookies) match {
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
      request.cookies.getModel[A] match {
        case Some(v) => f.fill(v) // Found cookie so fill the form with the cached data.
        case _ => f // No cookie found so return a blank form.
      }
  }
}
