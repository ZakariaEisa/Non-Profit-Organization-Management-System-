package controller;

import factory.NotificationStrategyFactory;
import model.notification.NotificationService;
import model.people.Person;

/**
 * Sends messages through the Strategy pattern; the channel can change at runtime.
 */
public class CommunicationController extends BaseController {
    private final NotificationService notificationService;

    public CommunicationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /** Send on an explicitly chosen channel ("Email", "SMS", "Social"). */
    public void sendMessage(String channel, String message, Person recipient) {
        notificationService.setStrategy(NotificationStrategyFactory.createStrategy(channel));
        notificationService.send(message, recipient);
    }

    /** CR-04: send on the recipient's own preferred channel. */
    public void sendToPreferredChannel(String message, Person recipient) {
        notificationService.sendUsingPreferredChannel(message, recipient);
    }
}
