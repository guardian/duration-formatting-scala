package com.gu.time.duration.formatting

import java.time.Duration
import java.time.temporal.ChronoUnit.SECONDS

trait DurationFormatter {
  def format(duration: Duration): String
}

object DurationFormatter {
  val Concise: DurationFormatter = _.truncatedTo(SECONDS).truncateToConcise.format(2)
}
