package hurricane

import com.github.tototoshi.csv.{CSVFormat, DefaultCSVFormat, QUOTE_MINIMAL, QUOTE_NONNUMERIC, Quoting}
import com.typesafe.config.ConfigFactory
import distage.config.ConfigModuleDef
import distage.{HasConstructor, ProviderMagnet}
import izumi.distage.config.AppConfigModule
import izumi.distage.effect.modules.ZIODIEffectModule
import izumi.distage.plugins.PluginDef
import org.http4s.HttpRoutes
import zio._

object HurricanePlugin extends PluginDef with ConfigModuleDef with ZIODIEffectModule {
  def provideHas[R: HasConstructor, A: Tag](fn: R => A): ProviderMagnet[A] =
    HasConstructor[R].map(fn)

  include(AppConfigModule(ConfigFactory.defaultApplication()))

  makeConfig[AppCfg]("app")

  make[String].named("csv").fromValue("hurricanes.csv")
  make[CSVFormat].fromValue(
    new DefaultCSVFormat {
      override val escapeChar: Char = '\''
    }
  )

  make[CsvReader].fromResource(CsvReader.make _)
  make[Logic].fromHas(Logic.make)

  make[Endpoints].fromValue(Endpoints.make)
  many[HttpRoutes[Task]]
    .addHas(Main.logicRoutes)
  make[HttpServer].fromHas(HttpServer.make _)
  make[UIO[Unit]].from(provideHas(Main.program.provide))
}
