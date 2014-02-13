package object modules {

  import play.api._
  import play.api.Play.current
  import com.google.inject.Guice

  // Play.isTest will evaluate to true when you run "play test" from the command line
  // If play is being run to execute the tests then use the TestModule to provide fake 
  // implementations of traits otherwise use the DevModule to provide the real ones
  /**
   * Application configuration is in a hierarchy of files:
   *
   * application.conf
   * /             |            \
   * application.prod.conf    application.dev.conf    application.test.conf <- these can override and add to application.conf
   *
   * play test  <- test mode picks up application.test.conf
   * play run   <- dev mode picks up application.dev.conf
   * play start <- prod mode picks up application.prod.conf
   *
   * To override and stipulate a particular "conf" e.g.
   * play -Dconfig.file=conf/application.test.conf run
   */
  def module = if (Play.isTest) TestModule else DevModule

  lazy val injector = Guice.createInjector(module)


}
