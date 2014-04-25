package services.session

import scala.reflect.ClassTag

class PlaySessionState extends SessionState {

  /**
   * Writes a value to the store.
   * @param key Key to store value against.
   * @param value Value to store.
   * @tparam T Value type.
   */
  override def set[T](key: String, value: Option[T]): Unit =
    if (value.isDefined)
      play.cache.Cache.set(key, value.get)
    else
      play.cache.Cache.remove(key)

  /**
   * Gets the value registered against the key.
   * @param key Key to retrieve value for.
   * @tparam T Value type.
   * @return Value associated against key, if it exists and is assignable to [[T]], otherwise [[None]].
   */
  override def get[T : ClassTag](key: String): Option[T] =
    play.cache.Cache.get(key) match {
      case value: T => Some(value)
      case _ => None
    }
}
