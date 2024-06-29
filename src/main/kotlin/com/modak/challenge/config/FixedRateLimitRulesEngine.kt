package com.modak.challenge.config

import com.modak.challenge.model.NotificationType
import com.modak.challenge.model.RateLimitRule
import java.time.temporal.ChronoUnit

class FixedRateLimitRulesEngine : RateLimitRulesEngine {
    override fun getRateLimitRules(): Map<NotificationType, RateLimitRule> {
        return mapOf(
            NotificationType("status") to RateLimitRule(2, ChronoUnit.MINUTES),    // 2 per minute
            NotificationType("news") to RateLimitRule(1, ChronoUnit.DAYS),         // 1 per day
            NotificationType("marketing") to RateLimitRule(3, ChronoUnit.HOURS)    // 3 per hour
        )
    }
}
