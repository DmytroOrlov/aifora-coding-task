package hurricane

import zio._
import zio.macros.accessible

@accessible
trait Endpoints {
  def most: UIO[MostHurricanes]

  def possibility: UIO[HurricanePossibility]
}

case class MostHurricanes(year: Int, month: Int)

case class HurricanePossibility(value: Double)

case class FailureResp(error: String)

case class Year(value: Int) extends AnyVal

case class Month(value: Int) extends AnyVal

object Endpoints {
  val most = ???

  val possibility = ???
}
