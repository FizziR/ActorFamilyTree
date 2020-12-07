name := "ActorFamiliyTree"

version := "0.1"


resolvers += Resolver.JCenterRepository
libraryDependencies += "net.katsstuff" %% "ackcord" % "0.17.1" //For high level API, includes all the other modules

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % "test"
val AkkaVersion = "2.6.6"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % Test
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion

