package hurricane

import distage.{HasConstructor, Injector, ModuleDef, ProviderMagnet}
import zio._
import zio.console._

object Main extends App {
  def run(args: List[String]) = {
    val program = for {
      _ <- putStrLn("123")
    } yield ()

    def provideHas[R: HasConstructor, A: Tag](fn: R => A): ProviderMagnet[A] =
      HasConstructor[R].map(fn)

    val module = new ModuleDef {
      make[Console.Service].fromHas(Console.live)

      make[UIO[Unit]].from(provideHas(program.provide))
    }

    Injector()
      .produceGetF[Task, UIO[Unit]](module)
      .useEffect
      .exitCode
  }
}
