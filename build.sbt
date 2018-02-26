name := "SendingMail"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= {
  val AkkaHttpVersion = "10.0.10"

  Seq(
    "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
    "org.apache.commons" % "commons-email" % "1.5"
  )
}