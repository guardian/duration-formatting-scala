package com.gu.time.duration.formatting

import java.time.Duration
import java.time.temporal.ChronoUnit.SECONDS

trait DurationFormatter extends (Duration => String)
