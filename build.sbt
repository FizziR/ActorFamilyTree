name := "ActorFamiliyTree"

version := "0.1"


resolvers += Resolver.JCenterRepository
libraryDependencies += "net.katsstuff" %% "ackcord" % "0.17.1" //For high level API, includes all the other modules

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % "test"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % "test"
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"
val AkkaVersion = "2.6.6"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % Test

libraryDependencies += "org.apache.kafka" %% "kafka" % "2.6.0"

libraryDependencies += "org.apache.spark" % "spark-core_2.12" % "3.0.1"
libraryDependencies += "org.apache.spark" % "spark-streaming_2.12" % "3.0.1"
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-10_2.12" % "3.0.1"
libraryDependencies += "org.apache.spark" % "spark-sql_2.12" % "3.0.1"

