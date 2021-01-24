import sttp.tapir.Codec.PlainCodec
import sttp.tapir.generic.Configuration

package object hurricane {
  implicit val tapirSnakeCaseConfig =
    Configuration.default.withSnakeCaseMemberNames

  implicit val yearCodec: PlainCodec[Year] =
    implicitly[PlainCodec[Int]].map(Year)(_.value)
  implicit val monthCodec: PlainCodec[Month] =
    implicitly[PlainCodec[String]].map(Month)(_.value)
}
