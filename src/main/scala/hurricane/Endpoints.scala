package hurricane

import io.circe.generic.auto._
import sttp.tapir._
import sttp.tapir.json.circe._
import zio._
import zio.macros.accessible

@accessible
trait Endpoints {
  def most: UIO[Endpoint[Unit, FailureResp, List[MostHurricanes], Nothing]]

  def possibility: UIO[Endpoint[Month, FailureResp, List[HurricanePossibility], Nothing]]
}

case class MostHurricanes(year: Int, month: Int)

case class HurricanePossibility(value: Double)

case class FailureResp(error: String)

case class Year(value: Int) extends AnyVal

case class Month(value: String) extends AnyVal

object Endpoints {
  val most = IO.succeed {
    endpoint.get
      .in("most")
      .out(jsonBody[List[MostHurricanes]])
      .errorOut(jsonBody[FailureResp])
  }

  val possibility = IO.succeed {
    endpoint.get
      .in("possibility")
      .in(path[Month]("month"))
      .out(jsonBody[List[HurricanePossibility]])
      .errorOut(jsonBody[FailureResp])
  }
}