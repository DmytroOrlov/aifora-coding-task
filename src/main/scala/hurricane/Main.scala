package hurricane

import buildinfo.BuildInfo.version
import distage.{HasConstructor, Injector, ModuleDef, ProviderMagnet}
import izumi.distage.effect.modules.ZIODIEffectModule
import org.http4s.HttpRoutes
import zio._
import zio.console._
import org.http4s.server.Router
import buildinfo.BuildInfo.version
import cats.syntax.semigroupk._
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
          .provide(env)
      }: HttpRoutes[Task]) <+> most.toRoutes { _ =>
        Logic.most
          .map {
            case (year, month) => MostHurricanes(year, month)
          }.provide(env)
      } <+>
        new SwaggerHttp4s(docs.toYaml).routes)
    )
  } yield router

  def run(args: List[String]) = {
    val program =
      HttpServer.bindHttp *> ZIO.never

    def provideHas[R: HasConstructor, A: Tag](fn: R => A): ProviderMagnet[A] =
      HasConstructor[R].map(fn)

    val module = new ModuleDef with ZIODIEffectModule {
      make[Console.Service].fromHas(Console.live)

      many[HttpRoutes[Task]]
        .addHas(Main.logicRoutes)
      make[HttpServer].fromHas(HttpServer.make _)
      make[UIO[Unit]].from(provideHas(program.provide))
    }

    Injector()
      .produceGetF[Task, UIO[Unit]](module)
      .useEffect
      .exitCode
  }
}
