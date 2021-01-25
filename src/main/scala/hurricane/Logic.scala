package hurricane

import capture.Capture
import capture.Capture.Constructors
import zio._
import zio.macros.accessible

@accessible
trait Logic {
  def most: IO[Capture[LogicErr], (Year, Month)]

  def possibility(month: Month): IO[FailureResp, Double]
}

object Logic {
  val make = for {
    hurricanes <- CsvReader.readHurricanes
    hs = hurricanes.toList.map(h => h.month -> h).toMap
  } yield new Logic {
    val most = for {
      res <- IO.fromOption(hs.values.toList.flatMap(h => h.hurricanes.toList.map { case (y, hs) => (y, h.month, hs) }).maxByOption(_._3))
        .bimap(
          _ => LogicErr.empty,
          { case (y, m, _) => y -> m }
        )
    } yield res

    def possibility(month: Month) = IO.succeed {
      0.1
    }
  }
}

trait LogicErr[A] {
  def empty: A
}

object LogicErr extends Constructors[LogicErr] {
  def empty = Capture[LogicErr](_.empty)

  trait AsFailureResp extends LogicErr[FailureResp] {
    def empty: FailureResp = FailureResp("cannot find max hurricanes in empty data")
  }

}
