package com.modak.challenge.model

import java.time.temporal.ChronoUnit

data class RateLimitRule(val maxCount: Int, val timeWindow: Long) { // timeWindow in seconds
    constructor(maxCount: Int, unit: ChronoUnit) : this(maxCount, unit.duration.seconds)
}
