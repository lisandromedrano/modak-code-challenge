package com.modak.challenge.service

import com.modak.challenge.config.RateLimitRulesEngine
import com.modak.challenge.model.NotificationType
import com.modak.challenge.model.RateLimitRule
import com.modak.challenge.util.TimeProvider
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

class RateLimiter(rateLimitRulesEngine: RateLimitRulesEngine, private val timeProvider: TimeProvider) {

    private val rules = rateLimitRulesEngine.getRateLimitRules()
    private val sentNotifications = mutableMapOf<String, MutableMap<String, Queue<Instant>>>()

    fun canSend(recipient: String, type: NotificationType): Boolean {
        val rule = rules[type] ?: return true // No limit set for this type

        val userNotifications = sentNotifications.computeIfAbsent(recipient) { mutableMapOf() }
        val userNotificationsByType = userNotifications.computeIfAbsent(type.name) { LinkedList() }

        val now = timeProvider.now()
        removeOldNotifications(userNotificationsByType, now, rule)

        return isWithinLimit(userNotificationsByType, rule, now)
    }

    private fun removeOldNotifications(userNotificationsByType: Queue<Instant>, now: Instant, rule: RateLimitRule) {
        val expirationTime = now.minus(rule.timeWindow.toLong(), ChronoUnit.SECONDS)
        while (userNotificationsByType.isNotEmpty() && userNotificationsByType.peek().isBefore(expirationTime)) {
            userNotificationsByType.poll()
        }
    }

    private fun isWithinLimit(userNotificationsByType: Queue<Instant>, rule: RateLimitRule, now: Instant): Boolean {
        val withinLimit = userNotificationsByType.size < rule.maxCount
        if (withinLimit) {
            userNotificationsByType.add(now)
        }
        return withinLimit
    }
}
