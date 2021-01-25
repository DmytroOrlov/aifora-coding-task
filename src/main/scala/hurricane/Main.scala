package hurricane

import buildinfo.BuildInfo.version
import cats.syntax.semigroupk._
import distage.Injector
import izumi.distage.plugins.PluginConfig
import izumi.distage.plugins.load.PluginLoader
import org.http4s.HttpRoutes
import org.http4s.server.Router
import sttp.tapir.docs.openapi._
import sttp.tapir.openapi.circe.yaml._
import sttp.tapir.server.http4s._
import sttp.tapir.swagger.http4s.SwaggerHttp4s
import zio._
import zio.interop.catz._

object Main extends App {
  val logicRoutes = for {
    implicit0(rts: Runtime[Any]) <- ZIO.runtime[Any]
    env <- ZIO.environment[Has[Logic]]
    most <- Endpoints.most
    possibility <- Endpoints.possibility
    docs = Seq(most, possibility).toOpenAPI("Hurricanes", version)
    router = Router[Task](
      "/" -> ((possibility.toRoutes { month =>
        Logic.possibility(month)
          .map(HurricanePossibility(_))
          .either
          .provide(env)
      }: HttpRoutes[Task]) <+> most.toRoutes { _ =>
        Logic.most
          .bimap(
            _.continue(new LogicErr.AsFailureResp {}), {
              case (year, month) => MostHurricanes(year, month)
            }).either
          .provide(env)
      } <+>
        new SwaggerHttp4s(docs.toYaml).routes)
    )
  } yield router

  val program = HttpServer.bindHttp *> ZIO.never

  def run(args: List[String]) = {
    val pluginConfig = PluginConfig.cached(
      packagesEnabled = Seq(
        "hurricane",
      )
    )
    val appModules = PluginLoader().load(pluginConfig)

    Injector()
      .produceGetF[Task, UIO[Unit]](appModules.merge)
      .useEffect
      .exitCode
  }
}

case class AppCfg(years: List[String]) {
  val readYears = years.map(y => Year(y.toInt))
}
