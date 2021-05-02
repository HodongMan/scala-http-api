import Dependencies._

ThisBuild / scalaVersion     := "2.12.10"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val akkaVersion = "2.6.10"
lazy val akkaHttpVersion = "10.2.2"

lazy val root = (project in file("."))
    .settings(
        name := "chickpea",
        libraryDependencies ++= Seq(
            "com.typesafe.akka" %% "akka-actor-typed"     % akkaVersion,
            "com.typesafe.akka" %% "akka-stream"          % akkaVersion,

            "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
            "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
            "com.typesafe.akka" %% "akka-http-xml"        % akkaHttpVersion,
            

            "ch.qos.logback" % "logback-classic" % "1.2.3",
            "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
            "org.scalatest" %% "scalatest" % "3.1.0" % Test,

            "de.heikoseeberger" %% "akka-http-circe" % "1.35.2", // Sugar for serialization and deserialization in akka-http with circe

            "ch.megard" %% "akka-http-cors" % "1.1.1", // Support of CORS requests, version depends on akka-http

            "com.zaxxer" % "HikariCP" % "2.3.2", // DB ConnectionPool
            "com.typesafe.slick" %% "slick" % "3.3.3", // SQL generator
            "org.flywaydb" % "flyway-core" % "7.3.2", // DB Migrate


            "com.github.pureconfig" %% "pureconfig" % "0.14.0", // config

            "com.pauldijou" %% "jwt-core" % "4.3.0", // jwt

            "io.circe" %% "circe-core" % "0.12.3", // json serializer
            "io.circe" %% "circe-parser" % "0.12.3", // json serializer
            "io.circe" %% "circe-generic" % "0.12.3", // json serializer


            "com.roundeights" % "hasher_2.12" % "1.2.0", // hash function

            "mysql" % "mysql-connector-java" % "8.0.22", // mysql connector
        )
    )
