package hurricane

import izumi.distage.testkit.scalatest.DistageBIOEnvSpecScalatest
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.matchers.must.Matchers
import org.scalatest.{EitherValues, OptionValues}
import zio._

class HurricaneTest extends DistageBIOEnvSpecScalatest[ZIO] with OptionValues with EitherValues with TypeCheckedTripleEquals with Matchers {
  "Logic" must {
    "fail for no data in most" in {
      (for {
        er <- Logic.most.either
        erMsg = er.left.value.continue(new LogicErr[String] {
          override def empty: String = "empty data"

          override def noPossibility(month: Month): String = ???
        })
        _ = assert(erMsg === "empty data")
      } yield ())
        .provideLayer(ZLayer.fromEffect(
          Logic.make.provide(
            Has(new CsvReader {
              val readHurricanes = UIO(Stream.empty)
            })
          )
        ))
    }
    "fail for absent month in possibility" in {
      for {
        er <- Logic.possibility(Month("Jan")).either
        erMsg = er.left.value.continue(new LogicErr[String] {
          override def empty: String = ???

          override def noPossibility(month: Month): String = s"no $month"
        })
        _ = assert(erMsg === "no Month(Jan)")
      } yield ()
    }
    "return possibility for May (average = 0.1)" in {
      for {
        may <- Logic.possibility(Month("May"))
        average = 0.1
        _ = assert(may === 1.0 - Math.pow(Math.E, -average))
      } yield ()
    }
    "return most = 8" in {
      for {
        (year, month) <- Logic.most

        hs <- CsvReader.readHurricanes
        hurricanes = hs.toList.map(h => h.month -> h).toMap.apply(month).hurricanes(year)
        _ = assert(hurricanes === 8)
      } yield ()
    }
  }
}
