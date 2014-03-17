package services

import helpers.UnitSpec

class CacheServiceSpec extends UnitSpec {
  val cacheService = new CacheServiceImpl()

  "new session" should {
    "generate a UUID for this journey" in pending
    "add session to the cache when not already in cache" in pending
    "replace existing session when already in cache" in pending
    "add headers to session to prevent click jacking" in pending

    // Is Carers generating a new withSession every time you change page? And populating it with a clone of journey so far.
  }

  "update form" should {
    "add form to cached when not already in cache" in pending
    "replace existing form when already in cache" in pending
  }

  "remove form" should {
    "remove form when it exists in cache" in pending
    "do nothing when form is not in cache"
  }

  "timeout" should {
    "remove session data from cache when time elapses" in pending
    "reset when user navigates between pages" in pending
  }
}
