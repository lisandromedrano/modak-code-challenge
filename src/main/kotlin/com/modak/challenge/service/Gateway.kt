package com.modak.challenge.service

interface Gateway {
    fun send(userId: String, message: String)
}