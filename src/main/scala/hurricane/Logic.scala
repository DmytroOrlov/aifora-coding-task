package hurricane

import capture.Capture
import capture.Capture.Constructors
import zio._
import zio.macros.accessible

@accessible
trait Logic {
  def most: IO[Capture[LogicErr], (Year, Month)]

  def possibility(month: Month): IO[Capture[LogicErr], Double]
}

object Logic {
  val make = for {
    hs <- CsvReader.readHurricanes
    hurricanes = hs.toList.map(h => h.month -> h).toMap
  } yield new Logic {
    val most = for {
      res <- IO.fromOption(hurricanes.values.toList.flatMap(h => h.hurricanes.toList.map { case (y, hs) => (y, h.month, hs) }).maxByOption(_._3))
        .bimap(
          _ => LogicErr.empty,
          { case (y, m, _) => y -> m }
        )
    } yield res

    def possibility(month: Month) = for {
      h <- IO.fromOption(hurricanes.get(month)).orElseFail(LogicErr.noPossibility(month))
    } yield 1.0 - Math.pow(Math.E, -h.average)
  }
}

trait LogicErr[A] {
  def empty: A

  def noPossibility(month: Month): A
}

object LogicErr extends Constructors[LogicErr] {
  def empty = Capture[LogicErr](_.empty)

  def noPossibility(month: Month) = Capture[LogicErr](_.noPossibility(month))

  trait AsFailureResp extends LogicErr[FailureResp] {
    def empty: FailureResp =
      FailureResp("cannot find max hurricanes in empty data")

    def noPossibility(month: Month): FailureResp =
      FailureResp(s"no average to estimate possibility in $month")
  }

}
