import io.circe.Codec
import io.circe.generic.extras.semiauto.deriveUnwrappedCodec
import sttp.tapir.Codec.PlainCodec

package object hurricane {
  implicit val `Codec[Month]`: Codec[Month] = deriveUnwrappedCodec
  implicit val `Codec[Year]`: Codec[Year] = deriveUnwrappedCodec

  implicit val `PlainCodec[Year]`: PlainCodec[Year] =
    implicitly[PlainCodec[Int]].map(Year)(_.value)
  implicit val `PlainCodec[Month]`: PlainCodec[Month] =
    implicitly[PlainCodec[String]].map(Month)(_.value)
}
