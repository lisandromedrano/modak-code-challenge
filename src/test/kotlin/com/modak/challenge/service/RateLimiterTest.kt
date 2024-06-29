package com.modak.challenge.service

import com.modak.challenge.config.FixedRateLimitRulesEngine
import com.modak.challenge.model.NotificationType
import com.modak.challenge.util.TimeProvider
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RateLimiterTest {
    private lateinit var rateLimiter: RateLimiter
    private lateinit var timeProvider: TimeProvider

    private val statusNotification = NotificationType("status")
    private val marketingNotification = NotificationType("marketing")
    private val newsNotification = NotificationType("news")

    @BeforeEach
    fun setUp() {
        timeProvider = Mockito.mock(TimeProvider::class.java)
        val rateLimitRulesEngine = FixedRateLimitRulesEngine()
        rateLimiter = RateLimiter(rateLimitRulesEngine, timeProvider)
    }

    @Test
    fun `should allow sending notifications under the limit`() {
        val recipient = "user@example.com"
        val now = Instant.parse("2023-01-01T00:00:00Z")
        Mockito.`when`(timeProvider.now()).thenReturn(now)

        assertTrue(rateLimiter.canSend(recipient, statusNotification))
        assertTrue(rateLimiter.canSend(recipient, statusNotification))
    }

    @Test
    fun `should block sending notifications over the limit`() {
        val recipient = "user@example.com"
        val now = Instant.parse("2023-01-01T00:00:00Z")
        Mockito.`when`(timeProvider.now()).thenReturn(now)

        assertTrue(rateLimiter.canSend(recipient, statusNotification))
        assertTrue(rateLimiter.canSend(recipient, statusNotification))

        assertFalse(rateLimiter.canSend(recipient, statusNotification))
    }

    @Test
    fun `should allow sending notifications again after time window`() {
        val recipient = "user@example.com"
        val now = Instant.parse("2023-01-01T00:00:00Z")
        val towMinutesLater = now.plus(2, ChronoUnit.MINUTES)
        Mockito.`when`(timeProvider.now()).thenReturn(now)

        assertTrue(rateLimiter.canSend(recipient, statusNotification))
        assertTrue(rateLimiter.canSend(recipient, statusNotification))
        assertFalse(rateLimiter.canSend(recipient, statusNotification))

        // Simulate waiting for the time window to pass
        Mockito.`when`(timeProvider.now()).thenReturn(towMinutesLater)

        assertTrue(rateLimiter.canSend(recipient, statusNotification))
    }

    @Test
    fun `should handle multiple notification types correctly`() {
        val recipient = "user@example.com"
        val now = Instant.parse("2023-01-01T00:00:00Z")
        val tomorrow = now.plus(2, ChronoUnit.DAYS)
        Mockito.`when`(timeProvider.now()).thenReturn(now)

        assertTrue(rateLimiter.canSend(recipient, statusNotification))
        assertTrue(rateLimiter.canSend(recipient, statusNotification))
        assertFalse(rateLimiter.canSend(recipient, statusNotification))

        assertTrue(rateLimiter.canSend(recipient, newsNotification))
        assertFalse(rateLimiter.canSend(recipient, newsNotification))
        // Simulate waiting for the time window to pass
        Mockito.`when`(timeProvider.now()).thenReturn(tomorrow)
        assertTrue(rateLimiter.canSend(recipient, newsNotification))

        Mockito.`when`(timeProvider.now()).thenReturn(now)
        assertTrue(rateLimiter.canSend(recipient, marketingNotification))
        assertTrue(rateLimiter.canSend(recipient, marketingNotification))
        assertTrue(rateLimiter.canSend(recipient, marketingNotification))
        assertFalse(rateLimiter.canSend(recipient, marketingNotification))
    }

    @Test
    fun testCanSendWithDifferentRecipients() {
        val recipient1 = "user1@example.com"
        val recipient2 = "user2@example.com"
        val now = Instant.parse("2023-01-01T00:00:00Z")
        Mockito.`when`(timeProvider.now()).thenReturn(now)

        assertTrue(rateLimiter.canSend(recipient1, statusNotification))
        assertTrue(rateLimiter.canSend(recipient1, statusNotification))
        assertFalse(rateLimiter.canSend(recipient1, statusNotification))

        assertTrue(rateLimiter.canSend(recipient2, statusNotification))
        assertTrue(rateLimiter.canSend(recipient2, statusNotification))
        assertFalse(rateLimiter.canSend(recipient2, statusNotification))

        assertTrue(rateLimiter.canSend(recipient1, newsNotification))
        assertFalse(rateLimiter.canSend(recipient1, newsNotification))

        assertTrue(rateLimiter.canSend(recipient2, newsNotification))
        assertFalse(rateLimiter.canSend(recipient2, newsNotification))

        assertTrue(rateLimiter.canSend(recipient1, marketingNotification))
        assertTrue(rateLimiter.canSend(recipient1, marketingNotification))
        assertTrue(rateLimiter.canSend(recipient1, marketingNotification))
        assertFalse(rateLimiter.canSend(recipient1, marketingNotification))

        assertTrue(rateLimiter.canSend(recipient2, marketingNotification))
        assertTrue(rateLimiter.canSend(recipient2, marketingNotification))
        assertTrue(rateLimiter.canSend(recipient2, marketingNotification))
        assertFalse(rateLimiter.canSend(recipient2, marketingNotification))
    }
}
