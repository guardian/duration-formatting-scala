package com.gu.time.duration.formatting

import java.time.temporal.ChronoUnit

case class UnitAmount(amount: Long, unit: ChronoUnit) {
  val padded: String = s"${unit.pad(amount)}${unit.symbol}"

  val unpadded: String = s"$amount${unit.symbol}"
}
