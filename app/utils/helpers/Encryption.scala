package utils.helpers

import javax.crypto.spec.{SecretKeySpec, IvParameterSpec}
import javax.crypto.Cipher
import java.nio.charset.StandardCharsets
import org.apache.commons.codec.binary.Base64
import java.security.SecureRandom
import play.api.Play
import play.api.libs.Codecs

trait Encryption {
  def decrypt(cipherText: String): String
  def encrypt(clearText: String): String
}

trait CookieEncryption extends Encryption

trait Hashing {
  def hash(clearText: String): String
}

trait CookieNameHashing extends Hashing

class Sha1Hash extends Hashing {
  override def hash(clearText: String): String = Codecs.sha1(clearText)
}

class NoHash extends Hashing {
  override def hash(clearText: String): String = clearText
}
class AesEncryption extends Encryption {
  // TODO decide which strength of AES encryption to use
  // in order to use AES 256 bit (uses a 32 byte key (32 * 8 = 256 bit)) you must install the unlimited strength policy jar
  // files into the jre at the moment we are using AES 128 bit (uses a 16 byte key (16 * 8 = 128 bit))
  private val secretKey256Bit = applicationSecretKey256Bit.take(256 / 8)
  private val secretKey128Bit = applicationSecretKey256Bit.take(128 / 8)

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

  private val initializationVectorSizeInBytes = 128 / 8
  private lazy val provider: Option[String] = getConfig("application.crypto.provider")
  private lazy val transformation: String = getConfig("application.crypto.aes.transformation").getOrElse("AES")
  private lazy val secretKeySpec = new SecretKeySpec(secretKey128Bit, "AES")

  override def decrypt(cipherText: String): String = {
    val initializationVectorWithCipherBytes = Base64.decodeBase64(cipherText)
    val (initializationVectorBytes, cipherBytes) = initializationVectorWithCipherBytes.splitAt(initializationVectorSizeInBytes)
    val initializationVector = new IvParameterSpec(initializationVectorBytes)
    val cipher = provider.fold(Cipher.getInstance(transformation))(p => Cipher.getInstance(transformation, p))
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, initializationVector)
    val clearTextBytes = cipher.doFinal(cipherBytes)
    new String(clearTextBytes, StandardCharsets.UTF_8)
  }

  override def encrypt(clearText: String): String = {
    val initializationVectorBytes = getSecureRandomBytes(initializationVectorSizeInBytes)
    val initializationVector = new IvParameterSpec(initializationVectorBytes)
    val cipher = provider.fold(Cipher.getInstance(transformation))(p => Cipher.getInstance(transformation, p))
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, initializationVector)
    val cipherBytes = cipher.doFinal(clearText.getBytes(StandardCharsets.UTF_8))
    Base64.encodeBase64String(initializationVectorBytes ++ cipherBytes)
  }

  private def getSecureRandomBytes(numberOfBytes: Int): Array[Byte] = {
    val random = new SecureRandom()
    val bytes = new Array[Byte](numberOfBytes)
    random.nextBytes(bytes)
    bytes
  }

  private def getConfig(key: String) = Play.maybeApplication.flatMap(_.configuration.getString(key))
}

class NoEncryption extends Encryption {
  override def decrypt(clearText: String): String = clearText

  override def encrypt(clearText: String): String = clearText
}
