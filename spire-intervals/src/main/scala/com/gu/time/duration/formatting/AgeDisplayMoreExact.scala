package com.gu.time.duration.formatting

import cats.kernel.Order
import spire.math._

import java.time.Duration
import java.time.temporal.ChronoUnit._

given Order[Duration] = Order.fromOrdering

object AgeDisplayMoreExact {

  def print(d: Duration): String = {
    d.truncatedTo(SECONDS).truncateToConcise.format(2)
  }

  def print(durationRange: Interval[Duration]): String = durationRange match {
    case Point(p) => print(p)
    case Above(lower, _) => s">${print(lower)}"
    case Below(upper, _) => s"<${print(upper)}"
    case Bounded(lower, upper, _) =>
      val bounds = Seq(lower, upper)
      def both(f: Duration => _): String = bounds.map(f).mkString("–")
      bounds.toSet.commonSuitableSingleUnit.fold(both(print)) { commonUnit =>
        both(_.dividedBy(commonUnit.getDuration)) + commonUnit.symbol
      }
    case _ => durationRange.toString // All or Empty! Unlikely to reach this case, but at least it's readable
  }
}