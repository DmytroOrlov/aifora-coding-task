package hurricane

import izumi.distage.testkit.scalatest.DistageBIOEnvSpecScalatest
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.matchers.must.Matchers
import org.scalatest.{EitherValues, OptionValues}
import zio.ZIO

class HurricaneTest extends DistageBIOEnvSpecScalatest[ZIO] with OptionValues with EitherValues with TypeCheckedTripleEquals with Matchers {
  "" must {
    "" in {
      for {
        _ <- zio.IO.unit
        _ = fail("123")
      } yield ()
    }
  }
}
