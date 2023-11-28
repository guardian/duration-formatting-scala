package com.gu.time.duration.formatting

import java.time.Duration
import java.time.temporal.ChronoUnit
import java.time.temporal.ChronoUnit._
import scala.collection.immutable.{SortedMap, SortedSet}

val UnitSymbols: SortedMap[ChronoUnit, String] = SortedMap(
  MILLIS -> "ms",
  SECONDS -> "s",
  MINUTES -> "m",
  HOURS -> "h",
  DAYS -> "d"
)

val AllUsefulUnits: SortedSet[ChronoUnit] = UnitSymbols.keySet

extension (durations: Set[Duration])
  def commonSuitableSingleUnit: Option[ChronoUnit] =
    durations.map(_.suitableSingleUnits).reduce(_ intersect _).maxOption
