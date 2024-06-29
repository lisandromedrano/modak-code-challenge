package com.modak.challenge.util

import java.time.Instant

class DefaultTimeProvider : TimeProvider {
    override fun now(): Instant = Instant.now()
}
