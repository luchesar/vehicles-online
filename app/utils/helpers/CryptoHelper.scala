package utils.helpers

import app.ConfigProperties._
import play.api.libs.{Codecs, Crypto}
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import org.apache.commons.codec.binary.Base64
import java.nio.charset.StandardCharsets

object CryptoHelper {
  private val encryptFields = getProperty("encryptFields", default = true)
  
  private val encryptCookies = getProperty("encryptCookies", default = true)

  private val staticSecret = getProperty("staticSecret", default = false)

  private val secretKey = if (staticSecret) "1234567890123456" else generateKey

  private def generateKey: String = UUID.randomUUID().toString.substring(0, 16)

  private lazy val secretKeySpec = {
    val raw = secretKey.getBytes("utf-8")
    new SecretKeySpec(raw, "AES")
  }

  def decryptAES(cipherText: String): String = if (encryptFields) Crypto.decryptAES(cipherText, secretKey) else cipherText
  def decryptCookie(cipherText: String): String = if (encryptCookies) decryptAESAsBase64(cipherText) else cipherText

  def encryptAES(clearText: String) = if (encryptFields) Crypto.encryptAES(clearText, secretKey) else clearText
  def encryptCookie(clearText: String) = if (encryptCookies) encryptAESAsBase64(clearText) else clearText

  def sha1Hash(v: String): String = Codecs.sha1(v)

  private def encryptAESAsBase64(clearText: String): String = {
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
    val cipherText = cipher.doFinal(clearText.getBytes(StandardCharsets.UTF_8))
    Base64.encodeBase64String(cipherText)
  }

  private def decryptAESAsBase64(cipherText: String): String = {
    val cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
    val clearTextBytes = cipher.doFinal(Base64.decodeBase64(cipherText))
    new String(clearTextBytes, StandardCharsets.UTF_8)
  }
}