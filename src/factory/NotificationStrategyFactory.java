package factory;

import model.notification.EmailStrategy;
import model.notification.INotificationStrategy;
import model.notification.SMSStrategy;
import model.notification.SocialMediaStrategy;

/**
 * FACTORY pattern: turns a channel name (from the NotificationChannel lookup) into a strategy.
 */
public class NotificationStrategyFactory {
    private NotificationStrategyFactory() {
    }

    public static INotificationStrategy createStrategy(String channel) {
        switch (channel.trim().toLowerCase()) {
            case "email":
                return new EmailStrategy();
            case "sms":
                return new SMSStrategy();
            case "social":
            case "social media":
                return new SocialMediaStrategy();
            default:
                throw new IllegalArgumentException("Unknown notification channel: " + channel);
        }
    }
}
