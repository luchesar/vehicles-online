// Comment to get more information during initialization
logLevel := Level.Debug

// The Typesafe repository 
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"

resolvers += "Maven 2" at "http://repo2.maven.org/maven2"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.2.2")

addSbtPlugin("net.litola" % "play-sass" % "0.3.0")

addSbtPlugin("de.johoop" % "jacoco4sbt" % "2.1.5")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.4.0")

//resolvers += "Templemore Repository" at "http://templemore.co.uk/repo/"
resolvers += "Nexus Repositroy" at "http://rep002-01.skyscape.preview-dvla.co.uk:8081/nexus/content/repositories/thirdparty/"

addSbtPlugin("templemore" % "sbt-cucumber-plugin" % "0.8.0")