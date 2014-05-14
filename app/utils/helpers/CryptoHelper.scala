package utils.helpers

import app.ConfigProperties._
import play.api.libs.Codecs
import javax.crypto.Cipher
import javax.crypto.spec.{IvParameterSpec, SecretKeySpec}
import org.apache.commons.codec.binary.{Hex, Base64}
import java.nio.charset.StandardCharsets
import play.api.Play
import java.security.SecureRandom
import play.api.mvc.{Cookie, SimpleResult, Request}
import Config.cookieMaxAge

object CryptoHelper {

  final lazy val SaltKey = CryptoHelper.encryptCookieName("FE291934-66BD-4500-B27F-517C7D77F26B")

  private val encryptFields = getProperty("encryptFields", default = true)
  
  private val encryptCookies = getProperty("encryptCookies", default = true)

  private val initializationVectorSizeInBytes = 128 / 8

  // TODO decide which strength of AES encryption to use
  // in order to use AES 256 bit (uses a 32 byte key (32 * 8 = 256 bit)) you must install the unlimited strength policy jar files into the jre
  // at the moment we are using AES 128 bit (uses a 16 byte key (16 * 8 = 128 bit))
  private val secretKey256Bit = applicationSecretKey256Bit.take(256 / 8)
  private val secretKey128Bit = applicationSecretKey256Bit.take(128 / 8)

  private val initializationVector128Bit = applicationSecretKey256Bit.drop(128 / 8).take(128 / 8)

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

  private lazy val applicationSecretKey256Bit: Array[Byte] = {
    val configKey = "application.secret256Bit"
    getConfig(configKey) match {
      case Some(base64EncodedApplicationSecret) =>
        val keySizeInBits = 256
        val decodedKeySizeInBytes = keySizeInBits / 8
        val applicationSecret = Base64.decodeBase64(base64EncodedApplicationSecret)

        if (applicationSecret.length != decodedKeySizeInBytes) {
          throw new Exception(s"Application secret key must be $keySizeInBits bits ($decodedKeySizeInBytes decoded bytes). Actual size in bytes was ${applicationSecret.length}.")
        }

        applicationSecret
      case None =>
        throw new Exception(s"Missing $configKey from config")
    }
  }

  def decryptAES(cipherText: String, decryptFields: Boolean = encryptFields): String = if (decryptFields) decryptAESAsBase64(cipherText) else cipherText
  def decryptCookie(cipherText: String, decryptCookies: Boolean = encryptCookies): String = if (decryptCookies) decryptAESAsBase64(cipherText) else cipherText

  def encryptAES(clearText: String, encryptFields: Boolean = encryptFields) = if (encryptFields) encryptAESAsBase64(clearText) else clearText
  def encryptCookie(clearText: String, encryptCookies: Boolean = encryptCookies) = if (encryptCookies) encryptAESAsBase64(clearText) else clearText
  def encryptCookieName(clearText: String, encryptCookies: Boolean = encryptCookies) = if (encryptCookies) sha1Hash(clearText) else clearText
  def newCookieNameSalt = if (encryptCookies) Hex.encodeHexString(CryptoHelper.getSecureRandomBytes(16)) else ""

  def getSaltFromRequest(request: Request[_]): Option[String] =
    request.cookies.get(SaltKey).map { cookie =>
      CryptoHelper.decryptCookie(cookie.value)
    }

  def ensureSaltInResult(result: SimpleResult)(implicit request: Request[_]): (SimpleResult, String) =
    CryptoHelper.getSaltFromRequest(request) match {
      case Some(saltFromRequest) =>
        (result, saltFromRequest)
      case None =>
        val newSalt = CryptoHelper.newCookieNameSalt
        if (newSalt.isEmpty)
          (result, newSalt)
        else {
          val newSaltCookie = createCookie(name = SaltKey,
            value = CryptoHelper.encryptCookie(newSalt))
          val resultWithSalt = result.withCookies(newSaltCookie)
          (resultWithSalt, newSalt)
        }
    }

  private def sha1Hash(clearText: String): String =
    Codecs.sha1(clearText)

  private def encryptAESAsBase64(clearText: String): String = {
    val initializationVectorBytes = getSecureRandomBytes(initializationVectorSizeInBytes)
    val initializationVector = new IvParameterSpec(initializationVectorBytes)
    val cipher = provider.fold(Cipher.getInstance(transformation))(p => Cipher.getInstance(transformation, p))
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, initializationVector)
    val cipherBytes = cipher.doFinal(clearText.getBytes(StandardCharsets.UTF_8))
    Base64.encodeBase64String(initializationVectorBytes ++ cipherBytes)
  }

  private def decryptAESAsBase64(cipherText: String): String = {
    val initializationVectorWithCipherBytes = Base64.decodeBase64(cipherText)
    val (initializationVectorBytes, cipherBytes) = initializationVectorWithCipherBytes.splitAt(initializationVectorSizeInBytes)
    val initializationVector = new IvParameterSpec(initializationVectorBytes)
    val cipher = provider.fold(Cipher.getInstance(transformation))(p => Cipher.getInstance(transformation, p))
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, initializationVector)
    val clearTextBytes = cipher.doFinal(cipherBytes)
    new String(clearTextBytes, StandardCharsets.UTF_8)
  }

  def createCookie(name: String, value: String) = Cookie(name = name,
    value = value,
    maxAge = Some(cookieMaxAge)/*,
    secure = true*/
  )
}
