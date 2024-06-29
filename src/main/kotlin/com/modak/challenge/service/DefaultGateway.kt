package com.modak.challenge.service

class DefaultGateway : Gateway {
    override fun send(userId: String, message: String) {
        println("Notification sent to $userId: $message")
    }
}