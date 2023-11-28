package com.gu.time.duration.formatting

import java.time.Duration.{ZERO, ofDays, ofMinutes}
import java.time.ZoneOffset.UTC
import java.time.temporal.ChronoUnit
import java.time.temporal.ChronoUnit.SECONDS
import java.time.{Duration, Instant, LocalDate}
import scala.math.Ordering.Implicits.*
import scala.math.{ceil, log10}

extension (duration: Duration)
  /**
   * The largest chronological unit that is not larger than this duration - the largest unit we would naturally use
   * to represent the duration without rounding upwards. Eg, for 59m59s, this would be MINUTES, for 1h or 3h, it would
   * be HOURS.
   */
  def largestNonZeroUnit: Option[ChronoUnit] = AllUsefulUnits.toSeq.findLast(_.getDuration <= duration)

  /**
   * Represent this duration as a series of non-zero unit amounts (eg 5 DAYS, 3 HOURS, 2 SECONDS). If the
   * duration is zero, the series will be empty.
   */
  def toUnitAmounts: LazyList[UnitAmount] = largestNonZeroUnit.fold(LazyList.empty[UnitAmount]) { unit =>
    val amount = duration.dividedBy(unit.getDuration)
    UnitAmount(amount, unit) #:: duration.minus(unit.getDuration.multipliedBy(amount)).toUnitAmounts
  }

  /**
   * Format this duration as String, with a limited number of chronological units. This method never rounds upwards,
   * so 1m59s is never expressed as '2m'.
   *
   * @param maxUnits starting with the most significant time unit of this duration, what is the maximum number of time
   *                 units we want to express this time with? eg three: '6h7m8s', two: '6h7m', or one: '6h'?
   * @param zeroUnit the unit to express a 'zero' duration in - eg, do we want '0m', or '0s'?
   * @return
   */
  def format(maxUnits: Int = 2, zeroUnit: ChronoUnit = SECONDS): String = {
    val unitAmounts = toUnitAmounts.take(maxUnits).toList
    unitAmounts.headOption.fold(UnitAmount(0, zeroUnit).unpadded) { head =>
      (head.unpadded +: unitAmounts.tail.map(_.padded)).mkString
    }
  }

  /**
   * Assuming that two durations can be accurately represented with the same single unit (allowing us to display
   * "15–20m" or "50–60s" to concisely denote a range of duration), we can identify that unit by working out what
   * sensible units are for each duration, and taking the the intersection of those two sets.
   *
   * This field works out what possible different units might desirably & accurately denote the duration, if it
   * *can* be denoted by a single unit.
   *
   * If this duration can be accurately represented with a single time unit (eg 1s, or 10m, or 3h, but not 3h1s),
   * return that unit. Additionally, if the duration exactly corresponds to 1 of that unit, also return its
   * sub-unit (eg, for 1m, return both MINUTES & SECONDS as suitable units for expressing the duration).
   */
  def suitableSingleUnits: Set[ChronoUnit] = largestNonZeroUnit.fold[Set[ChronoUnit]](AllUsefulUnits) { unit =>
    if (unit.getDuration == duration) {
      Set(unit) ++ unit.smallerUnit // tolerate doing '60s' rather than '1m'
    } else if (isPreciseWith(unit)) Set(unit) else Set.empty
  }

  def isPreciseWith(unit: ChronoUnit): Boolean = duration.truncatedTo(unit) == duration


  def truncateToConcise: Duration = largestNonZeroUnit.fold(ZERO) { unit =>
    val durationBenefitsFromSecondaryUnit = duration >= ofMinutes(1) && duration < ofDays(10)
    duration.truncatedTo(if (durationBenefitsFromSecondaryUnit) unit.smallerOrSameIfNoSmaller else unit)
  }

extension (chronoUnit: ChronoUnit)
  def symbol: String = UnitSymbols.getOrElse(chronoUnit, nameSingular)
  def namePlural: String = chronoUnit.name.toLowerCase
  def nameSingular: String = namePlural.stripSuffix("s")

  def smallerUnit: Option[ChronoUnit] = AllUsefulUnits.maxBefore(chronoUnit)
  def biggerUnit: Option[ChronoUnit] = AllUsefulUnits.minAfter(chronoUnit)
  def smallerOrSameIfNoSmaller: ChronoUnit = smallerUnit.getOrElse(chronoUnit)

  def padDigits: Int =
    biggerUnit.map(bu => ceil(log10(bu.getDuration.dividedBy(chronoUnit.getDuration).toDouble)).toInt).getOrElse(1)

  // new DecimalFormat("00").format(l)
  def pad(amount: Long): String = s"%02d".format(amount)
  
extension (instant: Instant)
  def utcLocalDate: LocalDate = instant.atZone(UTC).toLocalDate
  def utcEpochDay: Long = utcLocalDate.toEpochDay
