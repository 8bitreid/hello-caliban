package example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import caliban.interop.circe.AkkaHttpCirceAdapter
import caliban.{CalibanError, GraphQLInterpreter}
import zio.{Runtime, ZEnv}

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object ThePeoplesCalibanServer extends App with AkkaHttpCirceAdapter {

  implicit val system: ActorSystem                        = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val runtime: Runtime[ZEnv]                     = Runtime.default

  val interpreter: GraphQLInterpreter[zio.ZEnv, CalibanError] = runtime.unsafeRun(
    PeopleService
      .make(PeopleData.personDb)
      .memoize
      .use(layer => PersonApi.personApi.interpreter.map(_.provideCustomLayer(layer)))
  )

  val route =
    path("api" / "graphql") {
      adapter.makeHttpService(interpreter)
    } ~ path("graphiql") {
      getFromResource("graphiql.html")
    }

  val bindingFuture = Http().newServerAt("localhost", 8088).bindFlow(route)
  println(s"Server online at http://localhost:8088/graphiql\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
  /*
   * TODO add doobie ..
   */

  //  def upsertLanguage(name: String): ConnectionIO[UUID] =
  //    sql"""INSERT INTO languages(id, name) VALUES
  //         |(${UUID.randomUUID()}, $name)
  //         |ON CONFLICT ON CONSTRAINT languages_name_key
  //         |DO UPDATE SET name=languages.name RETURNING (id)
  //         |""".stripMargin.update.withUniqueGeneratedKeys[UUID]("id")
  //
  //  def insertLike(who: String, what: String): ConnectionIO[Unit] =
  //    upsertLanguage(what).flatMap { id =>
  //      sql"""INSERT INTO likes(id, language_id, username, created)
  //           |VALUES(${UUID.randomUUID()}, $id, $who)
  //           |""".stripMargin.update.run.void
  //    }

  //  val transactor: Transactor[Task] = Transactor.fromDriverManager(
  //    driver = "org.postgresql.Driver",
  //    url = "jdbc:postgresql:caliban",
  //    user = "reidmewborne",
  //    pass = ""
  //  )
}
