package com.modak.challenge.service

import com.modak.challenge.model.NotificationType


class DefaultNotificationService(private val rateLimiter: RateLimiter, private val gateway: Gateway) : NotificationService {
    fun sendNotification(recipient: String, type: NotificationType, message: String) {
        if (rateLimiter.canSend(recipient, type)) {
            gateway.send(recipient, message)
        } else {
            println("Notification rate limit exceeded for $recipient")
        }
    }
}
