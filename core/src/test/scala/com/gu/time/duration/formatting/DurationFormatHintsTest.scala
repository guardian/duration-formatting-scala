package com.gu.time.duration.formatting

import com.gu.time.duration.formatting.DurationFormatHints.AllUsefulUnits
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.time.Duration._
import java.time.temporal.ChronoUnit._

class DurationFormatHintsTest extends AnyFlatSpec with Matchers {

  it should "know what the most efficient (largest) unit is for representing a time" in {
    ZERO.largestNonZeroUnit shouldBe None
    ofMinutes(1).largestNonZeroUnit shouldBe Some(MINUTES)
    ofMinutes(61).largestNonZeroUnit shouldBe Some(HOURS)
  }

  it should "know what the single ideal unit is for a duration - if there is one" in {
    ofSeconds(5).suitableSingleUnits shouldBe Set(SECONDS) // '5s' - ie with just a single unit
    ofSeconds(60).suitableSingleUnits shouldBe Set(SECONDS, MINUTES) // Could do '60s' or '1m' - both single unit
    ofSeconds(70).suitableSingleUnits shouldBe empty // Always display as '1m10s', ie with *multiple* units

    ZERO.suitableSingleUnits shouldBe AllUsefulUnits // '0s', '0m', '0h', we can tolerate them all
  }

  it should "be able to break a duration down into unit amounts" in {
    ofSeconds(1).toUnitAmounts shouldBe Seq(UnitAmount(1, SECONDS))
    ofSeconds(60).toUnitAmounts shouldBe Seq(UnitAmount(1, MINUTES))
    ofSeconds(70).toUnitAmounts shouldBe Seq(UnitAmount(1, MINUTES), UnitAmount(10, SECONDS))
    ofHours(49).plusMinutes(6).plusSeconds(3).toUnitAmounts shouldBe Seq(
      UnitAmount(2, DAYS),
      UnitAmount(1, HOURS),
      UnitAmount(6, MINUTES),
      UnitAmount(3, SECONDS)
    )
  }

  it should "truncate to a concise representation" in {
    ofMillis(78).truncateToConcise.toUnitAmounts shouldBe Seq(UnitAmount(78, MILLIS))
    ofMillis(1000).truncateToConcise.toUnitAmounts shouldBe Seq(UnitAmount(1, SECONDS))
    ofMillis(1001).truncateToConcise.toUnitAmounts shouldBe Seq(UnitAmount(1, SECONDS))
    ofMillis(1999).truncateToConcise.toUnitAmounts shouldBe Seq(UnitAmount(1, SECONDS))
    ofMillis(2000).truncateToConcise.toUnitAmounts shouldBe Seq(UnitAmount(2, SECONDS))
    ofSeconds(59).truncateToConcise.toUnitAmounts shouldBe Seq(UnitAmount(59, SECONDS))
    ofSeconds(60).truncateToConcise.toUnitAmounts shouldBe Seq(UnitAmount(1, MINUTES))
    ofSeconds(61).truncateToConcise.toUnitAmounts shouldBe Seq(UnitAmount(1, MINUTES), UnitAmount(1, SECONDS))
    ofSeconds(119).truncateToConcise.toUnitAmounts shouldBe Seq(UnitAmount(1, MINUTES), UnitAmount(59, SECONDS))
    ofHours(24).truncateToConcise.toUnitAmounts shouldBe Seq(UnitAmount(1, DAYS))
    ofHours(25).truncateToConcise.toUnitAmounts shouldBe Seq(UnitAmount(1, DAYS), UnitAmount(1, HOURS))
    ofHours(51).truncateToConcise.toUnitAmounts shouldBe Seq(UnitAmount(2, DAYS), UnitAmount(3, HOURS))
    ofHours(241).truncateToConcise.toUnitAmounts shouldBe Seq(UnitAmount(10, DAYS))
  }
}
