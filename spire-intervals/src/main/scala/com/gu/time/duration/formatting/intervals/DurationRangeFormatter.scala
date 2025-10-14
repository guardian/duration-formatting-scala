package com.gu.time.duration.formatting.intervals

import cats.kernel.Order
import com.gu.time.duration.formatting._
import spire.math._

import java.time.Duration

trait DurationRangeFormatter extends (Interval[Duration] => String)

object DurationRangeFormatter {

  implicit val orderDuration: Order[Duration] = Order.fromComparable

  def from(durationFormatter: DurationFormatter): DurationRangeFormatter = {
    case Point(p) => durationFormatter(p)
    case Above(lower, _) => s">${durationFormatter(lower)}"
    case Below(upper, _) => s"<${durationFormatter(upper)}"
    case Bounded(lower, upper, _) =>
      val bounds = Seq(lower, upper)

      def both(f: Duration => _): String = bounds.map(f).mkString("–")

      DurationFormatHints.commonSuitableSingleUnit(bounds.toSet).fold(both(durationFormatter)) { commonUnit =>
        both(_.dividedBy(commonUnit.getDuration)) + commonUnit.symbol
      }
    case other => other.toString // All or Empty! Unlikely to reach this case, but at least it's readable
  }
}