package services.session

import scala.reflect.ClassTag

/**
 * Rewritable key-value store associated with the current HTTP client.
 */
trait SessionState {

  /**
   * Writes a value to the store.
   * @param key Key to store value against.
   * @param value Value to store.
   * @tparam T Value type.
   */
  def set[T](key: String, value: Option[T]): Unit

  /**
   * Gets the value registered against the key.
   * @param key Key to retrieve value for.
   * @tparam T Value type.
   * @return Value associated against key, if it exists and is assignable to [[T]], otherwise [[None]].
   */
  def get[T : ClassTag](key: String): Option[T]
}
