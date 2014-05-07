DVLA Vehicles Online
====================

`vehicles-online` is the Web frontend for disposing vehicles online, where 'disposing' implies changing a vehicle's
ownership through trade.

Architectural overview
----------------------

### Presentation layer

This project encapsulates the presentation layer of the application.

The codebase is predominantly [Scala][scala] and is implemented against [Play][play-framework]: a 'full stack' Web
framework for the JVM.

### Microservices

Most complex business decisions are deferred to a network of [RESTful][rest] microservices. These are maintained through
separate projects:

-   `ordnance-survey`
-   `vehicles-lookup`
-   `vehicles-dispose-fulfil`

These services are mocked for automated testing, but must be running locally for manual testing/development of dependant
components within the presentation layer.

### Secrets

All static private keys are held in the `secret-repository` project.

These keys are stored as encrypted config files allowing them to be checked into source control.

Some projects require unencrypted versions of these config files to run; the decryption process is described later.

Development environment
-----------------------

1.  Each project must be checked-out to the same directory using the following names:

    -   `vehicles-online`
    -   `vehicles-dispose-fulfil`
    -   `vehicles-lookup`
    -   `secret-repository`
    -   `locate-api-master`
    -   `ordnance-survey`

2.  JDK 1.7 must be installed

3.  Install SASS. The [current documentation][install-sass] suggests:

        sudo gem install sass

4.  Install SBT.  The [current documentation][install-sbt] suggests:

        brew install sbt

5.  Increase 'permanent generation space' requirements for SBT.

    Create the file `~/.sbtconfig` with the following content:

        SBT_OPTS="$SBT_OPTS -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled -XX:PermSize=256M -XX:MaxPermSize=512M"

6.  Decrypt secret keys:

        cd vehicles-online
        ./setup XYZ

    *where `XYZ` is an offline secret key obtained through a trusted team member*

Running the application
-----------------------

1.  Run the `vehicles-online` application:

        cd vehicles-online
        sbt run

2.  Open in Web browser:

        http://localhost:9000/

3.  Repeat *step 1* for each required microservice (if any).

[install-sass]: http://sass-lang.com/install "Install SASS"
[install-sbt]: http://www.scala-sbt.org/release/docs/Getting-Started/Setup.html#installing-sbt "Install SBT"
[rest]: https://www.ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm "REST"
[play-framework]: http://www.playframework.com/ "Play Framework"
[scala]: http://www.scala-lang.org/ "Scala Language"
