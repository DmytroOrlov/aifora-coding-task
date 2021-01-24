package hurricane

import zio._
import zio.console._

object Main extends App {
  def run(args: List[String]) = {
    val program = for {
      _ <- putStrLn("123")
    } yield ()

    program.exitCode
  }
}
