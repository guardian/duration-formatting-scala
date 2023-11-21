package com.gu.time.duration.formatting

import spire.math.Interval

import java.time.Duration

trait DurationRangeFormatter {
  def format(durationRange: Interval[Duration]): String
}
