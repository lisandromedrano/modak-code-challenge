package com.modak.challenge.config

import com.modak.challenge.model.NotificationType
import com.modak.challenge.model.RateLimitRule

interface RateLimitRulesEngine {
    fun getRateLimitRules(): Map<NotificationType, RateLimitRule>
}
