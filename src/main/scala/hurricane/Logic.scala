package hurricane

import zio._
import zio.macros.accessible

@accessible
trait Logic {
  def most: IO[FailureResp, List[(Year, Month)]]

  def possibility(month: Month): IO[FailureResp, Double]
}

object Logic {
  val make = for {
    hurricanes <- CsvReader.readHurricanes
    hs = hurricanes.toList.map(h => h.month -> h).toMap
  } yield new Logic {
    val most = IO.succeed {
      List(
        (Year(2020), Month("Sep"))
      )
    }

    def possibility(month: Month) = IO.succeed {
      0.1
    }
  }
}
