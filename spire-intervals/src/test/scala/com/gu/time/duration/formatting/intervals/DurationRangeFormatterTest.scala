package com.gu.time.duration.formatting.intervals

import com.gu.time.duration.formatting._
import com.gu.time.duration.formatting.intervals.DurationRangeFormatter._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import spire.math.Interval

import java.time.Duration.{ZERO, ofMinutes, ofSeconds}

class DurationRangeFormatterTest extends AnyFlatSpec with Matchers {

  it should "be able to display duration ranges" in {
    val rangeFormatter = DurationRangeFormatter.from(_.format())

    rangeFormatter(Interval.point(ZERO)) shouldBe "0s"
    rangeFormatter(Interval.above(ofMinutes(20))) shouldBe ">20m"
    rangeFormatter(Interval.above(ofMinutes(1))) shouldBe ">1m"

    rangeFormatter(Interval.openLower(ZERO, ofSeconds(1))) shouldBe "0–1s"
    rangeFormatter(Interval.openLower(ofMinutes(15), ofMinutes(20))) shouldBe "15–20m"
    rangeFormatter(Interval.openLower(ofMinutes(1), ofSeconds(70))) shouldBe "1m–1m10s"
    rangeFormatter(Interval.openLower(ofSeconds(50), ofMinutes(1))) shouldBe "50–60s"
  }
}
