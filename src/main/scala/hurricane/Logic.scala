package hurricane

import zio._
import zio.macros.accessible

@accessible
trait Logic {
  def most: UIO[(Year, Month)]

  def possibility(month: Month): UIO[Double]
}
