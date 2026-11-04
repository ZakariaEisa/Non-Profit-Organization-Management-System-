package model.notification;

import factory.NotificationStrategyFactory;
import model.people.Person;

/**
 * STRATEGY pattern: context. Holds the current strategy and can switch it at runtime.
 */
public class NotificationService {
    private INotificationStrategy strategy = new EmailStrategy();

    public void setStrategy(INotificationStrategy strategy) {
        this.strategy = strategy;
    }

    public void send(String message, Person recipient) {
        strategy.sendNotification(message, recipient);
    }

    /** CR-04: picks the strategy from the recipient's preferred channel (Email if none). */
    public void sendUsingPreferredChannel(String message, Person recipient) {
        String channel = recipient.getPreferredChannel() == null ? "Email" : recipient.getPreferredChannel().getName();
        setStrategy(NotificationStrategyFactory.createStrategy(channel));
        send(message, recipient);
    }
}
