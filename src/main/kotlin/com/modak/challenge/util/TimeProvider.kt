package com.modak.challenge.util

import java.time.Instant

interface TimeProvider {
    fun now(): Instant
}
