package common

import utils.helpers.{CookieNameHashing, CookieEncryption}
import play.api.mvc.Cookie

class EncryptedClientSideSession(sessionSecretKey: String)(implicit cookieFlags: CookieFlags, encryption: CookieEncryption, cookieNameHashing: CookieNameHashing) extends ClientSideSession {

  override def nameCookie(key: String): CookieName =
    CookieName(cookieNameHashing.hash(sessionSecretKey + key))

  override def newCookie(name: CookieName, value: String): Cookie = {
    val valueCoupledToName = name.value + value
    val cipherText = encryption.encrypt(valueCoupledToName)
    cookieFlags.applyToCookie(Cookie(
      name = name.value,
      value = cipherText))
  }

  override def getCookieValue(cookie: Cookie): String = {
    val cookieName = cookie.name
    val cipherText = cookie.value
    val valueCoupledToName = encryption.decrypt(cipherText)
    val (cookieNameFromPayload, value) = valueCoupledToName.splitAt(cookieName.length)
    assert(cookieName == cookieNameFromPayload, "The cookie name bytes from the payload must match the cookie name")
    value
  }
}
