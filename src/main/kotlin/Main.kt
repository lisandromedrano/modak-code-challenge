import com.modak.challenge.config.FixedRateLimitRulesEngine
import com.modak.challenge.model.NotificationType
import com.modak.challenge.service.DefaultGateway
import com.modak.challenge.service.DefaultNotificationService
import com.modak.challenge.service.RateLimiter
import com.modak.challenge.util.DefaultTimeProvider

fun main(args: Array<String>) {
    val rateLimitRulesEngine = FixedRateLimitRulesEngine()
    val timeProvider = DefaultTimeProvider()
    val rateLimiter = RateLimiter(rateLimitRulesEngine, timeProvider)
    val gateway = DefaultGateway()

    val notificationService = DefaultNotificationService(rateLimiter, gateway)

    val recipient = "user@example.com"

    // Using the same instances as in FixedRateLimitRulesEngine
    val statusNotification = NotificationType("status")
    val newsNotification = NotificationType("news")
    val marketingNotification = NotificationType("marketing")

    // Testing the rate-limited notification service
    notificationService.sendNotification(recipient, statusNotification, "Status update 1")
    notificationService.sendNotification(recipient, statusNotification, "Status update 2")
    notificationService.sendNotification(recipient, statusNotification, "Status update 3") // This should be rejected

    notificationService.sendNotification(recipient, newsNotification, "Daily news 1")
    notificationService.sendNotification(recipient, newsNotification, "Daily news 2") // This should be rejected

    notificationService.sendNotification(recipient, marketingNotification, "Marketing 1")
    notificationService.sendNotification(recipient, marketingNotification, "Marketing 2")
    notificationService.sendNotification(recipient, marketingNotification, "Marketing 3")
    notificationService.sendNotification(recipient, marketingNotification, "Marketing 4") // This should be rejected
}