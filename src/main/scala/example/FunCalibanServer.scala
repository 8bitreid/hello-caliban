package example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import caliban.interop.circe.AkkaHttpCirceAdapter
import caliban.{CalibanError, GraphQLInterpreter}
import zio.{Runtime, ZEnv}

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object FunCalibanServer extends App with AkkaHttpCirceAdapter {

  implicit val system: ActorSystem = ActorSystem()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  implicit val runtime: Runtime[ZEnv] = Runtime.default

  val interpreter: GraphQLInterpreter[zio.ZEnv, CalibanError] = runtime.unsafeRun(
    FunService
      .make(FunData.personDb)
      .memoize
      .use(layer => FunApi.funApi.interpreter.map(_.provideCustomLayer(layer)))
  )

  val route =
    path("api" / "graphql") {
      adapter.makeHttpService(interpreter)
    } ~ path("graphiql") {
      getFromResource("graphiql.html")
    }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8088)
  println(s"Server online at http://localhost:8088/graphiql\nPress RETURN to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
