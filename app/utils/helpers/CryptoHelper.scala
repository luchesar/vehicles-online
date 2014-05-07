package utils.helpers

import app.ConfigProperties._
import play.api.libs.Crypto
import java.util.UUID

object CryptoHelper {
  val encryptFields = getProperty("encryptFields", default = true)
  
  val encryptCookies = getProperty("encryptCookies", default = true)

  val staticSecret = getProperty("staticSecret", default = false)

  val secretKey = if (staticSecret) "1234567890123456" else generateKey

  private def generateKey: String = UUID.randomUUID().toString.substring(0, 16)

  def decryptAES(v: String): String = if (encryptFields) Crypto.decryptAES(v, secretKey) else v
  def decryptCookieAES(v: String): String = if (encryptCookies) Crypto.decryptAES(v, secretKey) else v

  def encryptAES(v: String) = if (encryptFields) Crypto.encryptAES(v, secretKey) else v
  def encryptCookieAES(v: String) = if (encryptCookies) Crypto.encryptAES(v, secretKey) else v

}