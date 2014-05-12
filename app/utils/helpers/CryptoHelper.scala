package utils.helpers

import app.ConfigProperties._
import play.api.libs.{Codecs, Crypto}
import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.spec.{IvParameterSpec, SecretKeySpec}
import org.apache.commons.codec.binary.{Hex, Base64}
import java.nio.charset.StandardCharsets
import play.api.{PlayException, Play}
import java.security.SecureRandom

object CryptoHelper {
  private val encryptFields = getProperty("encryptFields", default = true)
  
  private val encryptCookies = getProperty("encryptCookies", default = true)

  // TODO decide which strength of AES encryption to use
  // in order to use AES 256 bit (uses a 32 byte key (32 * 8 = 256 bit)) you must install the unlimited strength policy jar files into the jre
  // at the moment we are using AES 128 bit (uses a 16 byte key (16 * 8 = 128 bit))
  private val secretKey256Bit = getSecureRandomBytes(32)
  private val secretKey128Bit = getSecureRandomBytes(16)

  private val initializationVector128Bit = getSecureRandomBytes(16)

  private def getSecureRandomBytes(numberOfBytes: Int): Array[Byte] = {
    val random = new SecureRandom()
    val bytes = new Array[Byte](numberOfBytes)
    random.nextBytes(bytes)
    bytes
  }

  private lazy val secretKeySpec = new SecretKeySpec(secretKey128Bit, "AES")

  private lazy val initializationVector = new IvParameterSpec(initializationVector128Bit)

  private def getConfig(key: String) = Play.maybeApplication.flatMap(_.configuration.getString(key))
  private lazy val provider: Option[String] = getConfig("application.crypto.provider")
  private lazy val transformation: String = getConfig("application.crypto.aes.transformation").getOrElse("AES")

  def decryptAES(cipherText: String, decryptFields: Boolean = encryptFields): String = if (decryptFields) decryptAESAsBase64(cipherText) else cipherText
  def decryptCookie(cipherText: String, decryptCookies: Boolean = encryptCookies): String = if (decryptCookies) decryptAESAsBase64(cipherText) else cipherText

  def encryptAES(clearText: String, encryptFields: Boolean = encryptFields) = if (encryptFields) encryptAESAsBase64(clearText) else clearText
  def encryptCookie(clearText: String, encryptCookies: Boolean = encryptCookies) = if (encryptCookies) encryptAESAsBase64(clearText) else clearText
  def encryptCookieName(clearText: String, encryptCookies: Boolean = encryptCookies) = if (encryptCookies) sha1Hash(clearText) else clearText
  def newCookieNameSalt = if (encryptCookies) Hex.encodeHexString(CryptoHelper.getSecureRandomBytes(16)) else ""

  private def sha1Hash(clearText: String): String = Codecs.sha1(clearText)

  private def encryptAESAsBase64(clearText: String): String = {
    val cipher = provider.map(p => Cipher.getInstance(transformation, p)).getOrElse(Cipher.getInstance(transformation))
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, initializationVector)
    val cipherText = cipher.doFinal(clearText.getBytes(StandardCharsets.UTF_8))
    Base64.encodeBase64String(cipherText)
  }

  private def decryptAESAsBase64(cipherText: String): String = {
    val cipher = provider.map(p => Cipher.getInstance(transformation, p)).getOrElse(Cipher.getInstance(transformation))
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, initializationVector)
    val clearTextBytes = cipher.doFinal(Base64.decodeBase64(cipherText))
    new String(clearTextBytes, StandardCharsets.UTF_8)
  }

}
