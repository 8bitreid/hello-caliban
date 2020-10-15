import sbt._

object Dependencies {

  object Zio {
    lazy val dev = "dev.zio" %% "zio" % "1.0.3"
    lazy val catsInterop = "dev.zio" %% "zio-interop-cats" % "2.2.0.1"
  }

  object Caliban {
    private val calibanVersion = "0.9.2"
    lazy val caliban = "com.github.ghostdogpr" %% "caliban" % calibanVersion
    lazy val tapir = "com.github.ghostdogpr" %% "caliban-tapir" % calibanVersion
    lazy val http4s = "com.github.ghostdogpr" %% "caliban-http4s" % calibanVersion
    lazy val akka = "com.github.ghostdogpr" %% "caliban-akka-http" % calibanVersion
    lazy val circe = "de.heikoseeberger" %% "akka-http-circe" % "1.35.0"
  }

  object Doobie {
    private val doobieVersion = "0.9.0"
    lazy val psql = "org.tpolecat" %% "doobie-postgres" % doobieVersion
    lazy val hikari = "org.tpolecat" %% "doobie-hikari" % doobieVersion
    lazy val quill = "org.tpolecat" %% "doobie-quill" % doobieVersion
  }

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.8"
}
