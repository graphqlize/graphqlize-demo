val ScalatraVersion = "2.7.0"

organization := "org.graphqlize"

name := "Scalatra GraphQLize Demo Web App"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.10"

resolvers += Classpaths.typesafeReleases
resolvers += "Clojars" at "https://clojars.org/repo"

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.19.v20190610" % "container",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",

  "org.scalatra" %% "scalatra-json" % ScalatraVersion,
  "org.json4s"   %% "json4s-jackson" % "3.6.7",
  "org.postgresql" % "postgresql" % "42.2.10",
  "mysql" % "mysql-connector-java" % "8.0.19",
  "com.zaxxer" % "HikariCP" % "3.4.2",
  "org.graphqlize" % "graphqlize-java" % "0.1.0-alpha6"
)

enablePlugins(SbtTwirl)
enablePlugins(ScalatraPlugin)
