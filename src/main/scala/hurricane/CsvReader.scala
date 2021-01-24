package hurricane

import com.github.tototoshi.csv.{CSVFormat, CSVReader}
import distage.Id
import zio.macros.accessible
import zio.{IO, Task}

import scala.io.Source.fromResource

@accessible
trait CsvReader {
  def readHurricanes: Task[Stream[HurricaneLine]]
}

case class HurricaneLine(
    month: Month,
    average: Double,
    hurricanes: Map[Year, Int]
)

object CsvReader {
  def make(
      cfg: AppCfg,
      csv: String@Id("csv"),
      csvFmt: CSVFormat,
  ) = for {
    reader <- IO(CSVReader.open(fromResource(csv))(csvFmt)).toManaged(r => IO(r.close()).ignore)
    stringsStreamWithHeader = reader.toStream
    res <- IO {
      val stringsStream = stringsStreamWithHeader.tail
      stringsStream.map {
        case month :: average :: hurricanes =>
          HurricaneLine(
            Month(month),
            average.toDouble,
            cfg.readYears.zip(hurricanes).map { case (y, hs) => y -> hs.toInt }.toMap
          )
      }
    }.toManaged_
  } yield new CsvReader {
    def readHurricanes = IO.succeed {
      res
    }
  }
}
