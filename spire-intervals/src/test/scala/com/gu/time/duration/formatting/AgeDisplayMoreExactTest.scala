package com.gu.time.duration.formatting

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import spire.math.Interval

import java.time.Duration.{ZERO, ofMinutes, ofSeconds}
import AgeDisplayMoreExact._

class AgeDisplayMoreExactTest extends AnyFlatSpec with Matchers {
  it should "display a duration like this" in {
    AgeDisplayMoreExact.print(ofSeconds(0)) shouldBe "0s"
    AgeDisplayMoreExact.print(ofSeconds(1)) shouldBe "1s"
    AgeDisplayMoreExact.print(ofMinutes(1)) shouldBe "1m"
    AgeDisplayMoreExact.print(ofSeconds(69)) shouldBe "1m09s" // apparently we pad the secondary field!
    AgeDisplayMoreExact.print(ofSeconds(74)) shouldBe "1m14s"
  }

  it should "be able to display duration ranges" in {
    AgeDisplayMoreExact.print(Interval.point(ZERO)) shouldBe "0s"
    AgeDisplayMoreExact.print(Interval.above(ofMinutes(20))) shouldBe ">20m"

    AgeDisplayMoreExact.print(Interval.openLower(ZERO, ofSeconds(1))) shouldBe "0–1s"
    AgeDisplayMoreExact.print(Interval.openLower(ofMinutes(15), ofMinutes(20))) shouldBe "15–20m"
    AgeDisplayMoreExact.print(Interval.openLower(ofMinutes(1), ofSeconds(70))) shouldBe "1m–1m10s"
    AgeDisplayMoreExact.print(Interval.openLower(ofSeconds(50), ofMinutes(1))) shouldBe "50–60s" // non-essential style tweak
    // AgeDisplayMoreExact.print(Interval.openLower(ofSeconds(50), ofMinutes(1))) shouldBe "50–60s"
  }
}
